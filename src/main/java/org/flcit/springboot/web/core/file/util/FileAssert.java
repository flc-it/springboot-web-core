/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.springboot.web.core.file.util;

import java.io.IOException;

import org.flcit.springboot.web.core.exception.BadRequestException;
import org.springframework.core.io.Resource;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
public final class FileAssert {

    private FileAssert() { }

    /**
     * @param resource
     * @param maxLength
     * @param messageErreur
     * @throws IOException
     */
    public static void checkMaxLength(final Resource resource, final long maxLength, final String messageErreur) throws IOException {
        checkMaxLength(resource.contentLength(), maxLength, messageErreur);
    }

    /**
     * @param contentLength
     * @param maxLength
     * @param messageErreur
     */
    public static void checkMaxLength(final long contentLength, final long maxLength, final String messageErreur) {
        if (contentLength >= maxLength) {
            throw new BadRequestException(messageErreur);
        }
    }

}
