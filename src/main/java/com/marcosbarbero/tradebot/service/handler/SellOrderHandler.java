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

import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.model.dto.Quote;
import com.marcosbarbero.tradebot.model.dto.State;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.marcosbarbero.tradebot.commons.WebUtils.isHttpError;
import static com.marcosbarbero.tradebot.config.TradeBotProperties.SellPrice;
import static org.springframework.http.HttpMethod.DELETE;

/**
 * Handles the selling position {@link com.marcosbarbero.tradebot.model.dto.Action}.
 *
 * @author Marcos Barbero
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SellOrderHandler extends AbstractTradeHandler implements TradeHandler {

    private final RestTemplate restTemplate;
    private final TradeBotProperties tradeBotProperties;

    @Override
    public Quote handle(final Quote previous) {
        Quote quote = Quote.newInstance(previous);
        if (!shouldClosePosition(quote)) {
            return quote;
        }

        ResponseEntity<String> response = this.restTemplate.exchange(endpoint(), DELETE, null,
                String.class, quote.getPositionId());

        if (!isHttpError(response.getStatusCode())) {
            quote.setState(State.FINISHED);
            log.info("Position closed! Bought price {}, sold price: {}",
                    quote.getBoughtPrice(), quote.getCurrentPrice());
        }

        return quote;
    }

    private boolean shouldClosePosition(Quote quote) {
        SellPrice sellPrice = this.tradeBotProperties.getSellPrice();
        BigDecimal buyPrice = this.tradeBotProperties.getBuyPrice();
        BigDecimal lowerLimit = sellPrice.getLowerLimit();
        BigDecimal upperLimit = sellPrice.getUpperLimit();
        BigDecimal currentPrice = quote.getCurrentPrice();
        BigDecimal boughtPrice = quote.getBoughtPrice();

        log.debug("Buy price: {}, Lower sell price: {}, Upper sell price: {}, Current price: {}, Bought Price: {}",
                buyPrice, lowerLimit, upperLimit, currentPrice, boughtPrice);

        if (currentPrice.compareTo(buyPrice) < 0 && currentPrice.compareTo(boughtPrice) <= 0) {
            return false;
        }

        if (currentPrice.compareTo(lowerLimit) <= 0) {
            return false;
        }

        return upperLimit.compareTo(currentPrice) >= 0;
    }

    @Override
    protected String endpoint() {
        return this.tradeBotProperties.getEndpoints().getSellOrder();
    }
}

