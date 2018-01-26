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

package com.marcosbarbero.tradebot.config.listener;

import com.marcosbarbero.tradebot.config.TradeBotProperties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcos Barbero
 */
@Slf4j
//@Configuration
@RequiredArgsConstructor
public class StartUpListener implements InitializingBean {

    private final WebSocketConnectionManager connectionManager;

    private final TradeBotProperties tradeBotProperties;

    private final WebSocketClient client;

    private final WebSocketHandler webSocketHandler;

    private URI uri;

    @EventListener
    public void handleContextStarted(final ContextRefreshedEvent event) {

//        wsConnect();
    }

    // TODO: Add max-retry in the startup
    private void wsConnect() {
//        while (!isServerUp()) {
            while (!this.connectionManager.isRunning()) {
                this.connectionManager.start();
            }
//            this.connectionManager
//        }
    }

    private boolean isServerUp() {
        ListenableFuture<WebSocketSession> future =
                this.client.doHandshake(this.webSocketHandler,
                        new WebSocketHttpHeaders(this.connectionManager.getHeaders()), this.uri);

        future.addCallback(new ListenableFutureCallback<WebSocketSession>() {
            @Override
            public void onSuccess(WebSocketSession result) {
//                webSocketSession = result;
//                logger.info("Successfully connected");
            }
            @Override
            public void onFailure(Throwable ex) {
//                logger.error("Failed to connect", ex);
            }
        });
        return false;
    }

    //TODO: fix that replaceAll
    @Override
    public void afterPropertiesSet() throws Exception {
        String subscription = this.tradeBotProperties.getEndpoints().getSubscription();
        subscription = subscription.replaceAll("ws://", "http://");
        this.uri = URI.create(subscription);
    }
}
