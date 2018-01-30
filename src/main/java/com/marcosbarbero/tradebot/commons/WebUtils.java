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

import org.springframework.http.HttpStatus;

/**
 * Miscellaneous utilities for web applications.
 *
 * @author Marcos Barbero
 */
public final class WebUtils {

    private WebUtils() {
        // to avoid instantiation
    }

    /**
     * Returns if the given {@link HttpStatus} either in the CLIENT_ERROR or SERVER_ERROR.
     *
     * @param status The {@link HttpStatus}
     * @return TRUE if the given {@link HttpStatus} either in the CLIENT_ERROR or SERVER_ERROR, FALSE - otherwise
     */
    public static boolean isHttpError(final HttpStatus status) {
        return HttpStatus.Series.CLIENT_ERROR.equals(status.series())
                || HttpStatus.Series.SERVER_ERROR.equals(status.series());
    }
}
