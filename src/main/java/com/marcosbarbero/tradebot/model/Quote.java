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

package com.marcosbarbero.tradebot.model;

import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author Marcos Barbero
 */
@Data
public class Quote implements Cloneable {

    private BigDecimal currentPrice;

    public static Quote newInstance(Quote quote) {
        return ObjectUtils.clone(quote);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
