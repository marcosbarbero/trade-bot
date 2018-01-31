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

package com.marcosbarbero.tradebot.service;

import com.marcosbarbero.tradebot.config.websocket.data.QuotePayload;
import com.marcosbarbero.tradebot.model.dto.Quote;
import com.marcosbarbero.tradebot.model.dto.State;
import com.marcosbarbero.tradebot.model.repository.TradeRepository;
import com.marcosbarbero.tradebot.service.handler.TradeHandler;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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

    private final TradeHandler quoteHandler;
    private final TradeHandler buyOrderHandler;
    private final TradeHandler sellOrderHandler;
    private final TradeRepository tradeRepository;

    @Override
    public void execute(final WebSocketSession session, final QuotePayload payload, final CountDownLatch latch) {
        log.debug("WebSocket Payload: {}", payload);
        if (!isValid(payload)) {
            return;
        }

        Optional<Quote> latestState = this.tradeRepository.getLatestState();
        Quote quote = new Quote(payload);

        if (latestState.isPresent()) {
            Quote lastQuote = latestState.get();
            quote.setAction(lastQuote.getAction());
            quote.setPositionId(lastQuote.getPositionId());
            quote.setBoughtPrice(lastQuote.getBoughtPrice());
        }

        try {
            switch (quote.getAction()) {
                case QUOTE: {
                    quote = this.quoteHandler.handle(quote);
                }
                case BUY: {
                    quote = this.buyOrderHandler.handle(quote);
                }
                case SELL: {
                    quote = this.sellOrderHandler.handle(quote);
                }
            }
        } finally {
            if (quote.getState() == State.FINISHED) {
                close(session);
            }
            this.tradeRepository.save(quote);
            latch.countDown();
        }

    }

    private void close(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("An error occurred while closing the WebSocketSession.", e);
        }
    }

    private boolean isValid(QuotePayload payload) {
        return payload != null && payload.getT() != null && payload.getT().equals("trading.quote");
    }
}
