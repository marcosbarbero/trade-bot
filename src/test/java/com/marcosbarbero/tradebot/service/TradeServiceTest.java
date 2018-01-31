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

import org.junit.Test;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static com.marcosbarbero.tradebot.commons.TestUtils.quote;
import static com.marcosbarbero.tradebot.commons.TestUtils.quotePayload;
import static com.marcosbarbero.tradebot.model.dto.Action.BUY;
import static com.marcosbarbero.tradebot.model.dto.Action.SELL;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Marcos Barbero
 */
public class TradeServiceTest {

    private TradeHandler quoteHandler = mock(TradeHandler.class);
    private TradeHandler buyOrderHandler = mock(TradeHandler.class);
    private TradeHandler sellOrderHandler = mock(TradeHandler.class);
    private TradeRepository tradeRepository = mock(TradeRepository.class);

    private TradeService tradeService =
            new DefaultTradeService(quoteHandler, buyOrderHandler, sellOrderHandler, tradeRepository);

    @Test
    public void testInvalidPayload() {
        WebSocketSession session = mock(WebSocketSession.class);
        CountDownLatch latch = new CountDownLatch(1);

        QuotePayload quotePayload = quotePayload();
        quotePayload.setT(null);

        this.tradeService.execute(session, quotePayload, latch);

        verify(this.quoteHandler, never()).handle(any(Quote.class));
        verify(this.buyOrderHandler, never()).handle(any(Quote.class));
        verify(this.sellOrderHandler, never()).handle(any(Quote.class));
    }

    @Test
    public void testValidPayload() throws Exception {
        WebSocketSession session = mock(WebSocketSession.class);
        doNothing().when(session).close();

        CountDownLatch latch = new CountDownLatch(1);

        QuotePayload quotePayload = quotePayload();

        Quote quote = quote();
        when(this.tradeRepository.getLatestState()).thenReturn(Optional.of(quote));

        Quote buyQuote = Quote.newInstance(quote);
        buyQuote.setAction(BUY);
        when(this.quoteHandler.handle(any(Quote.class))).thenReturn(buyQuote);

        Quote sellQuote = Quote.newInstance(buyQuote);
        sellQuote.setAction(SELL);
        when(this.buyOrderHandler.handle(any(Quote.class))).thenReturn(sellQuote);

        Quote finishedQuote = Quote.newInstance(buyQuote);
        finishedQuote.setState(State.FINISHED);
        when(this.sellOrderHandler.handle(any(Quote.class))).thenReturn(finishedQuote);

        this.tradeService.execute(session, quotePayload, latch);

        verify(this.quoteHandler, times(1)).handle(any(Quote.class));
        verify(this.buyOrderHandler, times(1)).handle(any(Quote.class));
        verify(this.sellOrderHandler, times(1)).handle(any(Quote.class));
    }
}