package com.marcosbarbero.tradebot.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosbarbero.tradebot.config.TradeBotProperties;
import com.marcosbarbero.tradebot.model.dto.Action;
import com.marcosbarbero.tradebot.model.dto.Quote;
import com.marcosbarbero.tradebot.model.dto.api.ApiResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.marcosbarbero.tradebot.commons.TestUtils.apiResponse;
import static com.marcosbarbero.tradebot.commons.TestUtils.quote;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.notNull;

/**
 * @author Marcos Barbero
 */
public class BuyOrderHandlerTest {

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private BuyOrderHandler tradeHandler;

    @Before
    public void setUp() {
        TradeBotProperties properties = new TradeBotProperties();
        properties.getEndpoints().setBuyOrder("http://localhost:8080/buy/position/endpoint");

        this.tradeHandler = new BuyOrderHandler(this.restTemplate, properties, this.objectMapper);
    }


    @Test
    public void testHandleSuccess() throws Exception {
        Quote quote = quote();
        ApiResponse apiResponse = apiResponse();

        String responseBody = this.objectMapper.writeValueAsString(apiResponse);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);

        when(this.restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        Quote result = this.tradeHandler.handle(quote);

        assertEquals(apiResponse.getPositionId(), result.getPositionId());
        assertEquals(Action.SELL, result.getAction());
        assertEquals(quote.getCurrentPrice(), result.getBoughtPrice());
    }

    @Test
    public void testHandleFailure() throws Exception {
        Quote quote = quote();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(this.restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        Quote result = this.tradeHandler.handle(quote);

        assertNull(result.getPositionId());
        assertNotEquals(Action.SELL, result.getAction());
        assertNull(result.getBoughtPrice());
    }

    @Test
    public void endpoint() {
        notNull(this.tradeHandler.endpoint(), "Trade endpoint shouldn't be null.");
    }
}