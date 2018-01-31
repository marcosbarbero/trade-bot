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

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.marcosbarbero.tradebot.commons.TestUtils.quote;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notNull;

/**
 * @author Marcos Barbero
 */
public class SellOrderHandlerTest {


    private RestTemplate restTemplate = mock(RestTemplate.class);

    private SellOrderHandler tradeHandler;
    private TradeBotProperties properties;

    @Before
    public void setUp() throws Exception {
        this.properties = new TradeBotProperties();
        this.properties.getEndpoints().setSellOrder("http://localhost:8080/close/position/endpoint");
        this.properties.setAtomicMaxRetries(new AtomicInteger(1));
        this.properties.setBuyPrice(BigDecimal.valueOf(190.00));
        this.properties.getSellPrice().setUpperLimit(BigDecimal.valueOf(200.00));
        this.properties.getSellPrice().setLowerLimit(BigDecimal.valueOf(180.00));

        this.tradeHandler = new SellOrderHandler(this.restTemplate, properties);
    }

    @Test
    public void handleSuccessRequestMakingProfit() {
        Quote quote = quote();
        quote.setPositionId(UUID.randomUUID().toString());
        quote.setBoughtPrice(quote.getCurrentPrice().subtract(BigDecimal.valueOf(5)));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class), anyString())).thenReturn(responseEntity);

        assertEquals(State.RUNNING, quote.getState());

        Quote result = this.tradeHandler.handle(quote);
        assertEquals(State.FINISHED, result.getState());
    }

    @Test
    public void endpoint() {
        notNull(this.tradeHandler.endpoint(), "Sell endpoint shouldn't be null.");
    }

    @Test
    public void handleSuccessRequestMakingLoss() {
        Field atomicMaxRetries = ReflectionUtils.findField(TradeBotProperties.class, "atomicMaxRetries");
        ReflectionUtils.makeAccessible(atomicMaxRetries);
        ReflectionUtils.setField(atomicMaxRetries, this.properties, new AtomicInteger(0));

        Field tradeBotProperties = ReflectionUtils.findField(SellOrderHandler.class, "tradeBotProperties");
        ReflectionUtils.makeAccessible(tradeBotProperties);
        ReflectionUtils.setField(tradeBotProperties, this.tradeHandler, this.properties);

        Field retry = ReflectionUtils.findField(SellOrderHandler.class, "retry");
        ReflectionUtils.makeAccessible(retry);
        ReflectionUtils.setField(retry, this.tradeHandler, new AtomicInteger(1));

        Quote quote = quote();
        quote.setPositionId(UUID.randomUUID().toString());
        quote.setCurrentPrice(BigDecimal.valueOf(150.00));
        quote.setBoughtPrice(quote.getCurrentPrice().add(BigDecimal.valueOf(5)));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class), anyString())).thenReturn(responseEntity);

        assertEquals(State.RUNNING, quote.getState());

        Quote result = this.tradeHandler.handle(quote);
        assertEquals(State.FINISHED, result.getState());
    }
}