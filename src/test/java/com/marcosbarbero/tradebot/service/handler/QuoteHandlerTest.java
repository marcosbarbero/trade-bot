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

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.marcosbarbero.tradebot.commons.TestUtils.quote;
import static com.marcosbarbero.tradebot.model.dto.Action.BUY;
import static com.marcosbarbero.tradebot.model.dto.Action.QUOTE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Marcos Barbero
 */
public class QuoteHandlerTest {

    private TradeHandler tradeHandler;

    @Before
    public void setUp() {
        TradeBotProperties properties = new TradeBotProperties();
        properties.setBuyPrice(BigDecimal.valueOf(200.00));

        this.tradeHandler = new QuoteHandler(properties);
    }

    @Test
    public void testActionChangedToBuy() {
        Quote quote = quote();
        assertThat(quote.getAction(), is(QUOTE));

        quote = this.tradeHandler.handle(quote);

        assertThat(quote.getAction(), is(BUY));
    }

    @Test
    public void testActionStaysAsQuote() {
        Quote quote = quote(201.00);
        assertThat(quote.getAction(), is(QUOTE));

        quote = this.tradeHandler.handle(quote);

        assertThat(quote.getAction(), is(QUOTE));
    }

}