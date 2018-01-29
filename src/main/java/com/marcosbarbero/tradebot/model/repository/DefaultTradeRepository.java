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

import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A default implementation for {@link TradeRepository}.
 *
 * @author Marcos Barbero
 */
@Repository
public class DefaultTradeRepository implements TradeRepository {

    /**
     * For now it's not necessary to keep multiple quotes, because it's not allowed by the application design
     * to configure multiple productIds to quote, in a further implementation this `storage` strategy to proper data
     * storage.
     */
    private static Quote quote;

    @Override
    public Optional<Quote> getLatestState() {
        return Optional.ofNullable(DefaultTradeRepository.quote);
    }

    @Override
    public void save(final Quote quote) {
        DefaultTradeRepository.quote = quote;
    }
}
