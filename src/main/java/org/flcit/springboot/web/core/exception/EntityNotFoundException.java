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

package org.flcit.springboot.web.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
@SuppressWarnings("java:S110")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_ENTITY_NOT_FOUND = "ENTITY %s WITH ID %s NOT FOUND";
    private static final String MESSAGE_ENTITY_CODE_NOT_FOUND = "ENTITY %s WITH CODE %s NOT FOUND";

    /**
     * @param table
     * @param code
     */
    public EntityNotFoundException(String table, String code) {
        super(String.format(MESSAGE_ENTITY_CODE_NOT_FOUND, table, org.flcit.commons.core.util.StringUtils.convertOrNull(code)));
    }

    /**
     * @param table
     * @param id
     */
    public EntityNotFoundException(String table, Object id) {
        super(String.format(MESSAGE_ENTITY_NOT_FOUND, table, org.flcit.commons.core.util.StringUtils.convertOrNull(id)));
    }

    /**
     * @param table
     * @param ids
     */
    public EntityNotFoundException(String table, Object... ids) {
        this(table, StringUtils.arrayToDelimitedString(ids, org.flcit.commons.core.util.StringUtils.PIPE));
    }

    /**
     * @param cause
     */
    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

}
