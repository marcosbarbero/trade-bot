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

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.marcosbarbero.tradebot.model.dto.Action.BUY;

/**
 * Handles the quoting {@link com.marcosbarbero.tradebot.model.dto.Action}.
 *
 * @author Marcos Barbero
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteHandler implements TradeHandler {

    private final TradeBotProperties tradeBotProperties;

    @Override
    public Quote handle(final Quote previous) {
        Quote quote = Quote.newInstance(previous);

        log.info("Quoting - current price: {}", quote.getCurrentPrice());

        if (quote.getCurrentPrice().compareTo(this.tradeBotProperties.getBuyPrice()) <= 0) {
            quote.setAction(BUY);
            log.info("QUOTED - current price: {}, buy price: {}",
                    quote.getCurrentPrice(), this.tradeBotProperties.getBuyPrice());
        }
        return quote;
    }
}
