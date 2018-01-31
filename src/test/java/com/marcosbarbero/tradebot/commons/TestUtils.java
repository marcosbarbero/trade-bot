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

package com.marcosbarbero.tradebot.commons;

import com.marcosbarbero.tradebot.config.websocket.data.QuotePayload;
import com.marcosbarbero.tradebot.model.dto.Action;
import com.marcosbarbero.tradebot.model.dto.Quote;
import com.marcosbarbero.tradebot.model.dto.api.ApiResponse;
import com.marcosbarbero.tradebot.model.dto.api.Money;
import com.marcosbarbero.tradebot.model.dto.api.Type;

import java.math.BigDecimal;

import static java.util.UUID.randomUUID;

/**
 * @author Marcos Barbero
 */
public final class TestUtils {

    private TestUtils() {
    }

    public static ApiResponse apiResponse() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDateCreated(System.currentTimeMillis());
        apiResponse.setDirection(Action.BUY);
        apiResponse.setId(randomUUID().toString());
        apiResponse.setPositionId(randomUUID().toString());
        apiResponse.setInvestingAmount(new Money("BUX", 2, BigDecimal.valueOf(200.00)));
        apiResponse.setLeverage(2);
        apiResponse.setType(Type.OPEN);
        return apiResponse;
    }

    public static Quote quote(double... price) {
        Quote quote = new Quote();
        double currentPrice = (price != null && price.length > 0) ? price[0] : 200.00;
        quote.setCurrentPrice(BigDecimal.valueOf(currentPrice));
        return quote;
    }

    public static QuotePayload quotePayload(double... price) {
        double currentPrice = (price != null && price.length > 0) ? price[0] : 200.00;
        QuotePayload quotePayload = new QuotePayload();
        quotePayload.setT("trading.quote");
        quotePayload.setBody(new QuotePayload.Body(BigDecimal.valueOf(currentPrice)));
        return quotePayload;
    }
}
