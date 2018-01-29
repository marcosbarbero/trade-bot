/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marcosbarbero.tradebot.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.model.dto.Quote;
import com.marcosbarbero.tradebot.model.dto.api.ApiResponse;
import com.marcosbarbero.tradebot.model.dto.api.Money;
import com.marcosbarbero.tradebot.model.dto.api.Trade;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.marcosbarbero.tradebot.commons.WebUtils.isHttpError;
import static com.marcosbarbero.tradebot.model.dto.Action.BUY;
import static com.marcosbarbero.tradebot.model.dto.Action.SELL;

/**
 * Handles the buying order {@link com.marcosbarbero.tradebot.model.dto.Action}.
 *
 * @author Marcos Barbero
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BuyOrderHandler extends AbstractTradeHandler implements TradeHandler {

    private final RestTemplate restTemplate;
    private final TradeBotProperties tradeBotProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Quote handle(final Quote previous) {
        Quote quote = Quote.newInstance(previous);

        Trade trade = Trade.builder()
                .direction(BUY)
                .investingAmount(new Money("BUX", 2, BigDecimal.valueOf(200.00)))
                .productId(this.tradeBotProperties.getProductId())
                .leverage(2)
                .source("NEWS_ITEM")
                .build();

        ResponseEntity<String> response = this.restTemplate.postForEntity(endpoint(), trade, String.class);

        if (!isHttpError(response.getStatusCode())) {
            try {
                ApiResponse apiResponse = this.objectMapper.readValue(response.getBody(), ApiResponse.class);
                quote.setPositionId(apiResponse.getPositionId());
                quote.setAction(SELL);
                quote.setBoughtPrice(quote.getCurrentPrice());

                log.info("Making a trade - Bought price: {}", quote.getBoughtPrice());
            } catch (IOException e) {
                log.warn("Could not read the response: {}", response.getBody());
            }
        }

        return quote;
    }

    @Override
    protected String endpoint() {
        return this.tradeBotProperties.getEndpoints().getBuyOrder();
    }

}
