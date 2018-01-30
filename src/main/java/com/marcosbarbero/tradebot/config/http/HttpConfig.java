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

package com.marcosbarbero.tradebot.config.http;

import com.marcosbarbero.tradebot.commons.SSLUtils;
import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.config.http.handler.DefaultApiErrorHandler;
import com.marcosbarbero.tradebot.config.http.intercept.DefaultHttpRequestInterceptor;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;

/**
 * Http configuration.
 *
 * @author Marcos Barbero
 */
@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate restTemplate(final TradeBotProperties tradeBotProperties) {
        final RestTemplate restTemplate = new RestTemplate(requestFactory());
        restTemplate.setInterceptors(singletonList(new DefaultHttpRequestInterceptor(tradeBotProperties)));
        restTemplate.setErrorHandler(new DefaultApiErrorHandler());
        return restTemplate;
    }

    private HttpComponentsClientHttpRequestFactory requestFactory() {
        final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(SSLUtils.sslContext());
        final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }
}
