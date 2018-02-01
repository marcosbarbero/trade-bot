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
import com.marcosbarbero.tradebot.commons.SSLUtils;
import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.config.handler.ShutdownHandler;
import com.marcosbarbero.tradebot.service.TradeService;

import org.apache.tomcat.websocket.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.Map;

import static org.apache.tomcat.websocket.Constants.SSL_CONTEXT_PROPERTY;

/**
 * WebSocket client configuration.
 *
 * @author Marcos Barbero
 */
@Configuration
public class WebSocketClientConfig {

    @Bean
    public WebSocketClient webSocketClient() {
        final StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        standardWebSocketClient.getUserProperties().put(SSL_CONTEXT_PROPERTY, SSLUtils.sslContext());
        return standardWebSocketClient;
    }

    @Bean
    public WebSocketHandler webSocketHandler(final TradeBotProperties tradeBotProperties,
                                             final ObjectMapper objectMapper,
                                             final TradeService tradeService,
                                             final ShutdownHandler shutdownHandler) {
        return new DefaultWebSocketHandler(tradeBotProperties, objectMapper, tradeService, shutdownHandler);
    }

    @Bean
    public WebSocketConnectionManager connectionManager(final WebSocketClient webSocketClient,
                                                        final WebSocketHandler webSocketHandler,
                                                        final TradeBotProperties tradeBotProperties) {
        final WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(webSocketClient,
                webSocketHandler, tradeBotProperties.getEndpoints().getSubscription());
        connectionManager.setHeaders(headers(tradeBotProperties));
        connectionManager.setAutoStartup(tradeBotProperties.isAutoConnect());
        return connectionManager;
    }

    private HttpHeaders headers(TradeBotProperties tradeBotProperties) {
        final HttpHeaders httpHeaders = new WebSocketHttpHeaders();
        for (Map.Entry<String, String> header : tradeBotProperties.getHeaders().entrySet()) {
            httpHeaders.add(header.getKey(), header.getValue());
        }
        return httpHeaders;
    }

}
