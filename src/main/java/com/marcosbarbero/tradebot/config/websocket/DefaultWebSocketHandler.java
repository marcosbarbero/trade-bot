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

package com.marcosbarbero.tradebot.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.config.handler.ShutdownHandler;
import com.marcosbarbero.tradebot.config.websocket.data.MessagePayload;
import com.marcosbarbero.tradebot.config.websocket.data.QuotePayload;
import com.marcosbarbero.tradebot.service.TradeService;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Default handler for the WebSocket connection.
 *
 * @author Marcos Barbero
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultWebSocketHandler extends TextWebSocketHandler {

    private static final int EXIT_CODE = 500;

    private final TradeBotProperties tradeBotProperties;

    private final ObjectMapper objectMapper;

    private final TradeService tradeService;

    private final ShutdownHandler shutdownHandler;

    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        MessagePayload payload = new MessagePayload(this.tradeBotProperties.getProductId());
        TextMessage message = new TextMessage(this.objectMapper.writeValueAsString(payload));
        session.sendMessage(message);
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        try {
            synchronized (this.latch) {
                this.latch.await(this.tradeBotProperties.getInterval(), MILLISECONDS);
                QuotePayload payload = this.objectMapper.readValue(message.getPayload(), QuotePayload.class);
                validateConnection(message, session);
                this.tradeService.execute(session, payload, this.latch);
            }
        } catch (IOException ex) {
            log.error("Error while parsing the message payload: {}", ex);
        }
    }

    @Override
    public void handleTransportError(final WebSocketSession session, final Throwable exception) throws Exception {
        log.error("WebSocket connection error: {}", exception);
    }

    private void validateConnection(TextMessage textMessage, WebSocketSession session) {
        if (textMessage.getPayload().contains("connect.failed")) {
            log.error("An error occurred to connect to the WebSocket: \n {}", textMessage.getPayload());
            this.shutdownHandler.initiateShutdown(EXIT_CODE, session);
        }
    }

}
