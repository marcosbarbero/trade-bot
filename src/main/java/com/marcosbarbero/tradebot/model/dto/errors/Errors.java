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

package com.marcosbarbero.tradebot.model.dto.errors;

import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Errors codes mapping for the API request.
 *
 * @author Marcos Barbero
 */
public enum Errors {

    AUTH_007(UNAUTHORIZED.value(), "access token is not valid"),

    AUTH_008(UNAUTHORIZED.value(), "access token is expired"),

    AUTH_009(UNAUTHORIZED.value(), "missing Authorization header"),

    AUTH_014(FORBIDDEN.value(), "user does not have sufficient permissions to perform this action"),

    CORE_00(NOT_FOUND.value(), "user not found"),

    CORE_002(INTERNAL_SERVER_ERROR.value(), "unexpected core error"),

    CORE_007(INTERNAL_SERVER_ERROR.value(), "unexpected trading error"),

    CORE_009(INTERNAL_SERVER_ERROR.value(), "user account is in an invalid state (no account on Ayondo side)"),

    CORE_010(INTERNAL_SERVER_ERROR.value(), "position with provided id not found in user portfolio"),

    CORE_011(INTERNAL_SERVER_ERROR.value(), "requested trade currency is different than home currency"),

    CORE_023(UNAUTHORIZED.value(), "user is required to enter his pincode to make a trade"),

    CORE_055(BAD_REQUEST.value(), "the selected trade amount is not allowed"),

    CORE_056(BAD_REQUEST.value(), "the selected multiplier is not allowed"),

    TRADING_007(BAD_REQUEST.value(), "market is still closed for the product the user tried to trade on"),

    TRADING_008(BAD_REQUEST.value(), "leverage is above the maximum allowed"),

    TRADING_009(BAD_REQUEST.value(), "exposure is above the maximum allowed"),

    TRADING_010(BAD_REQUEST.value(), "user does not have sufficient funds to complete the transaction (currently this is calculated by Ayondo by using their internal margin rules)"),

    TRADING_011(BAD_REQUEST.value(), "exposure is below the minimum allowed (this shouldn't happen with amounts greater than (or equal to) 10 euros, but Ayondo currently mis-configured some of the products)");

    @Getter
    private int statusCode;

    @Getter
    private String possibleReason;

    Errors(int statusCode, String possibleReason) {
        this.statusCode = statusCode;
        this.possibleReason = possibleReason;
    }
}
