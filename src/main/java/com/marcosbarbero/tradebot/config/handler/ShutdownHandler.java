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

package com.marcosbarbero.tradebot.config.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcos Barbero
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ShutdownHandler {

    private final ApplicationContext applicationContext;

    /**
     * Closes the given {@link WebSocketSession} and exit the application with the given returnCode.
     *
     * @param returnCode Any int value
     * @param session The current {@link WebSocketSession}
     */
    public void initiateShutdown(final int returnCode, final WebSocketSession session) {
        try {
            session.close();
        } catch (IOException ex) {
            log.warn("Error occurred while closing the WebSocket session.", ex);
        }
        SpringApplication.exit(applicationContext, () -> returnCode);
    }
}
