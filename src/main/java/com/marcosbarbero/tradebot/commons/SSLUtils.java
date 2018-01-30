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

import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Miscellaneous utilities for SSL connections.
 *
 * @author Marcos Barbero
 */
@Slf4j
public final class SSLUtils {

    private SSLUtils() {
        // avoid instantiation
    }

    /**
     * Returns a {@link SSLContext} that skips the validation.
     *
     * @return SSLContext
     */
    public static SSLContext sslContext() {
        final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        try {
            return SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("Could not create a SSL Context", e);
        }
        return SSLContexts.createDefault();
    }
}
