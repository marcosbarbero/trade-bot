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

package com.marcosbarbero.tradebot.config.http.handler;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import static com.marcosbarbero.tradebot.commons.WebUtils.isHttpError;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class to handle the REST calls errors.
 *
 * @author Marcos Barbero
 */
@Slf4j
public class DefaultApiErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(final ClientHttpResponse response) throws IOException {
        return isHttpError(response.getStatusCode());
    }

    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        log.error("Response error: {} {} {}", response.getStatusCode(), response.getStatusText(),
                IOUtils.toString(response.getBody(), UTF_8));
    }
}
