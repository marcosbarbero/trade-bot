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

package com.marcosbarbero.tradebot.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Configuration properties wrapper for `trade.api` namespace.
 *
 * @author Marcos Barbero
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "trade.api")
public class TradeBotProperties implements InitializingBean {

    private String productId;

    private BigDecimal buyPrice;

    private Integer maxRetries = 20;

    private Long interval = SECONDS.toMillis(1);

    private boolean autoConnect = true;

    @NestedConfigurationProperty
    private SellPrice sellPrice = new SellPrice();

    @NestedConfigurationProperty
    private Endpoints endpoints = new Endpoints();

    private Map<String, String> headers = new HashMap<>();

    private AtomicInteger atomicMaxRetries;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(headers, "The property 'trade.api.headers' cannot be empty");
        Assert.notNull(productId, "The property 'trade.api.productId' cannot be null");
        Assert.notNull(buyPrice, "The property 'trade.api.buyPrice' cannot be null");
        Assert.notNull(sellPrice.lowerLimit, "The property 'trade.api.sellPrice.lowerLimit' cannot be null");
        Assert.notNull(sellPrice.upperLimit, "The property 'trade.api.sellPrice.upperLimit' cannot be null");
        Assert.notNull(endpoints.subscription, "The property 'trade.api.endpoints.subscription' cannot be null");
        Assert.notNull(maxRetries, "The property 'trade.api.maxRetries' cannot be null");

        atomicMaxRetries = new AtomicInteger(maxRetries);

        checkTradeRules();
    }

    private void checkTradeRules() {
        /*
         Note that for the trading logic to be valid, the relation between the buy price and the lower / upper limit
         should be: lower limit sell price < buy price < upper limit sell price. Think about what it means to close a
         position with a profit. What should the relation between the current price and the upper limit selling price
         should be when deciding to close the position or not?
         */

        if (this.sellPrice.lowerLimit.compareTo(this.buyPrice) >= 0) {
            String message = format("The property 'trade.api.sellPrice.lowerLimit' [%f] needs to be lower than " +
                    "the value defined in 'trade.api.buyPrice' [%f]", this.sellPrice.lowerLimit, this.buyPrice);
            throw new IllegalArgumentException(message);
        }

        if (this.sellPrice.upperLimit.compareTo(this.buyPrice) <= 0) {
            String message = format("The property 'trade.api.sellPrice.upperLimit' [%f] needs to be greater than the " +
                    "value defined in 'trade.api.buyPrice' [%f]", this.sellPrice.upperLimit, this.buyPrice);
            throw new IllegalArgumentException(message);
        }

        log.info("PRODUCT ID: {}", this.productId);
    }

    @Data
    public static class SellPrice {
        private BigDecimal upperLimit;
        private BigDecimal lowerLimit;
    }

    @Data
    public class Endpoints {
        private String subscription;
        private String buyOrder;
        private String sellOrder;
    }
}
