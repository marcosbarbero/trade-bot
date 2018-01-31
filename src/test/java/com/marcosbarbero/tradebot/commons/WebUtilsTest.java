package com.marcosbarbero.tradebot.commons;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.marcosbarbero.tradebot.commons.WebUtils.isHttpError;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcos Barbero
 */
public class WebUtilsTest {

    @Test
    public void testIsHttpError() {
        boolean isNotHttpError = isHttpError(HttpStatus.OK);
        assertFalse(isNotHttpError);

        boolean isClientError = isHttpError(HttpStatus.BAD_REQUEST);
        assertTrue(isClientError);

        boolean isServerError = isHttpError(HttpStatus.INTERNAL_SERVER_ERROR);
        assertTrue(isServerError);
    }

}