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

package com.marcosbarbero.bux.tradebot.config.websocket.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

import static java.lang.String.format;

/**
 * Payload structure for quote updates.
 *
 * @author Marcos Barbero
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class MessagePayload {

    private static final String QUOTE_PREFIX = "trading.product.%s";

    @Getter
    private Set<String> subscribeTo;

    public MessagePayload(String productId) {
        this.subscribeTo = Collections.emptySet();

        if (StringUtils.isNotBlank(productId)) {
            this.subscribeTo = Collections.singleton(format(QUOTE_PREFIX, productId));
        }
    }

}
