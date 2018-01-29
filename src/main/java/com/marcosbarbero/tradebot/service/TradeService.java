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

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.CountDownLatch;

/**
 * Service interface to perform the trade operations.
 *
 * @author Marcos Barbero
 */
public interface TradeService {

    /**
     * Execute the operations to complete the whole trade flow.
     *
     * @param session {@link WebSocketSession}
     * @param payload {@link QuotePayload}
     * @param latch   {@link CountDownLatch}
     */
    void execute(WebSocketSession session, QuotePayload payload, CountDownLatch latch);
}
