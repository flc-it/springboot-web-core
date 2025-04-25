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

package org.flcit.springboot.web.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WrapperPageableFilterTest {

    @Test
    void testDefaultOk() {
        final WrapperPageableFilter<?> pageable = new WrapperPageableFilter<>();
        assertNull(pageable.getFilter());
        assertNull(pageable.getPageable());
        assertNull(pageable.getSearch());
        assertFalse(pageable.hasFilter());
        assertFalse(pageable.hasSearch());
    }

    @Test
    void testOk() {
        final WrapperPageableFilter<Object> pageable = new WrapperPageableFilter<>();
        final Object filter = new Object();
        final WrapperPageable page = new WrapperPageable();
        final String search = "search";
        pageable.setFilter(filter);
        pageable.setPageable(page);
        pageable.setSearch(search);
        assertEquals(filter, pageable.getFilter());
        assertEquals(page, pageable.getPageable());
        assertEquals(search, pageable.getSearch());
        assertTrue(pageable.hasFilter());
        assertTrue(pageable.hasSearch());
    }

}