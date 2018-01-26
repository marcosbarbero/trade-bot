package com.marcosbarbero.bux.tradebot.model;

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
