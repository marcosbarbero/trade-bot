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

package com.marcosbarbero.tradebot.model.repository;

import com.marcosbarbero.tradebot.model.dto.Quote;

import java.util.Optional;

/**
 * @author Marcos Barbero
 */
public interface TradeRepository {

    /**
     * Returns the latest saved a {@link Quote}.
     *
     * @return Optional of a nullable {@link Quote}
     */
    Optional<Quote> getLatestState();

    /**
     * Saves the given {@link Quote}.
     */
    void save(Quote quote);
}
