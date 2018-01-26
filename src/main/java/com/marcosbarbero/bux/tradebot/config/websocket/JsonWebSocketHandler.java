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

package com.marcosbarbero.bux.tradebot.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosbarbero.bux.tradebot.config.TradeBotProperties;
import com.marcosbarbero.bux.tradebot.config.websocket.data.MessagePayload;
import com.marcosbarbero.bux.tradebot.config.websocket.data.ResponsePayload;
import com.marcosbarbero.bux.tradebot.service.TradeService;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcos Barbero
 */
@Slf4j
@RequiredArgsConstructor
public class JsonWebSocketHandler extends TextWebSocketHandler {

    private final TradeBotProperties tradeBotProperties;

    private final ObjectMapper objectMapper;

    private final TradeService tradeService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        MessagePayload payload = new MessagePayload(this.tradeBotProperties.getProductId());
        TextMessage message = new TextMessage(this.objectMapper.writeValueAsString(payload));
        session.sendMessage(message);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ResponsePayload payload = null;
        try {
            payload = this.objectMapper.readValue(message.getPayload(), ResponsePayload.class);
        } catch (IOException ex) {
            log.error("Error while parsing the message payload: {}", ex);
        }
        this.tradeService.doTrade(session, payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket connection error: {}", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.error("WebSocket connection error: {}");
    }

}
