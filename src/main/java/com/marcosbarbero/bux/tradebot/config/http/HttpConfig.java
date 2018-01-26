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

package com.marcosbarbero.bux.tradebot.config.http;

import com.marcosbarbero.bux.tradebot.config.TradeBotProperties;
import com.marcosbarbero.bux.tradebot.config.http.intercept.HttpRequestInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Http configuration.
 *
 * @author Marcos Barbero
 */
@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate restTemplate(final TradeBotProperties tradeBotProperties) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new HttpRequestInterceptor(tradeBotProperties)));
        return restTemplate;
    }
}
