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
import java.util.concurrent.atomic.AtomicInteger;

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
public class SellOrderHandler extends AbstractTradeHandler implements TradeHandler {

    private final RestTemplate restTemplate;
    private final TradeBotProperties tradeBotProperties;

    private AtomicInteger retry;

    private AtomicInteger attempts = new AtomicInteger(0);

    public SellOrderHandler(final RestTemplate restTemplate, final TradeBotProperties tradeBotProperties) {
        this.restTemplate = restTemplate;
        this.tradeBotProperties = tradeBotProperties;
        this.retry = new AtomicInteger(this.tradeBotProperties.getMaxRetries());
    }

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
            BigDecimal currentPrice = quote.getCurrentPrice();
            BigDecimal boughtPrice = quote.getBoughtPrice();

            String profitOrLoss = boughtPrice.compareTo(currentPrice) < 0 ? "PROFIT :)" : "LOSS :(";
            log.info("POSITION CLOSED With {} !! Bought price {}, sold price: {}",
                    profitOrLoss, boughtPrice, currentPrice);
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

        log.info("Attempt n. {} - current price: {}", this.attempts.incrementAndGet(), currentPrice);

        log.debug("Buy price: {}, Lower sell price: {}, Upper sell price: {}, Current price: {}, Bought Price: {}",
                buyPrice, lowerLimit, upperLimit, currentPrice, boughtPrice);

        AtomicInteger atomicMaxRetries = this.tradeBotProperties.getAtomicMaxRetries();

        if (atomicMaxRetries.getAndDecrement() > 0) {
            return currentPrice.compareTo(upperLimit) >= 0;
        } else {
            int remainingAttempts = this.retry.decrementAndGet();

            if (currentPrice.compareTo(buyPrice) >= 0) {
                return true;
            }

            if (currentPrice.compareTo(boughtPrice) >= 0) {
                return true;
            }

            if (remainingAttempts == 0 && currentPrice.compareTo(lowerLimit) > 0) {
                return true;
            }

            return remainingAttempts == 0;
        }
    }

    @Override
    protected String endpoint() {
        return this.tradeBotProperties.getEndpoints().getSellOrder();
    }
}

