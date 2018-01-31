package com.marcosbarbero.tradebot.commons;

import org.junit.Test;

import javax.net.ssl.SSLContext;

import static org.junit.Assert.*;

/**
 * @author Marcos Barbero
 */
public class SSLUtilsTest {

    @Test
    public void sslContext() {
        SSLContext sslContext = SSLUtils.sslContext();
        assertNotNull(sslContext);
    }
}