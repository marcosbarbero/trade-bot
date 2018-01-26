/*
 * Copyright 2012-2017 the original author or authors.
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

package com.marcosbarbero.tradebot.service;

import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.config.websocket.data.ResponsePayload;
import com.marcosbarbero.tradebot.service.handler.TradeHandler;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link TradeService}'s default implementation.
 *
 * @author Marcos Barbero
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultTradeService implements TradeService {

    private final TradeBotProperties tradeBotProperties;
    private final TradeHandler quoteHandler;

    // TODO: Remove this throws
    @Override
    public void doTrade(WebSocketSession session, ResponsePayload payload) throws Exception {
        log.info("Invalid Payload: {}", payload);
        if (!isValid(payload)) {
            return;
        }
        doQuote(payload);
    }

    private void doQuote(ResponsePayload payload) {
        BigDecimal currentPrice = currentPrice(payload);
        if (currentPrice.compareTo(this.tradeBotProperties.getBuyPrice()) <= 0) {
//            this.quote.setCurrentPrice(currentPrice);
        }
    }

    private BigDecimal currentPrice(ResponsePayload payload) {
        return new BigDecimal(payload.getBody().get("currentPrice").toString());
    }

    private boolean isValid(ResponsePayload payload) {
        return payload != null && payload.getT() != null && payload.getT().equals("trading.quote");
    }
}
