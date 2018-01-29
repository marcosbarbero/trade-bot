package com.marcosbarbero.tradebot.model.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.marcosbarbero.tradebot.model.dto.Action;

import lombok.Data;

/**
 * POJO representation for the successful API responses.
 *
 * @author Marcos Barbero
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

    private String id;
    private String positionId;
    private int leverage;
    private Action direction;
    private Type type;
    private long dateCreated;
    private Money investingAmount;
    private Money price;
    private Money profitAndLoss;
    private Product product;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Product {
        private String securityId;
        private String symbol;
        private String displayName;
    }
}