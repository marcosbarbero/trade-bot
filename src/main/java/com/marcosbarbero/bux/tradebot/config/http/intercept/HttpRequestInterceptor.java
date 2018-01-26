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

package com.marcosbarbero.bux.tradebot.config.http.intercept;

import com.marcosbarbero.bux.tradebot.config.TradeBotProperties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * Interceptor to add default Http Headers to the {@link org.springframework.web.client.RestTemplate}.
 *
 * @author Marcos Barbero
 */
@RequiredArgsConstructor
public class HttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final TradeBotProperties tradeBotProperties;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();
        addHeaders(headers);
        return execution.execute(httpRequest, body);
    }

    private void addHeaders(HttpHeaders httpHeaders) {
        for (Map.Entry<String, String> header : this.tradeBotProperties.getHeaders().entrySet()) {
            httpHeaders.add(header.getKey(), header.getValue());
        }
    }
}
