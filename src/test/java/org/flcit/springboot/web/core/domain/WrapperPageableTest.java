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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

class WrapperPageableTest {

    @Test
    void testDefaultOk() {
        final WrapperPageable pageable = new WrapperPageable();
        assertEquals(0, pageable.getPageNumber());
        assertEquals(0, pageable.getOffset());
        assertEquals(0, pageable.getPageSize());
        assertEquals(Sort.unsorted(), pageable.getSort());
        assertNull(pageable.next());
        assertNull(pageable.previousOrFirst());
        assertNull(pageable.first());
        assertNull(pageable.withPage(1));
        assertFalse(pageable.hasPrevious());
        assertFalse(pageable.isPaged());
    }

    @Test
    void testOk() {
        final WrapperPageable pageable = new WrapperPageable();
        pageable.setPageNumber(2);
        pageable.setPageSize(20);
        final WrapperOrder order1 = new WrapperOrder();
        order1.setName("name");
        order1.setDirection(Direction.ASC);
        pageable.setOrders(new WrapperOrder[] { order1 });
        assertEquals(2, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertEquals(2 * 20, pageable.getOffset());
        assertEquals(Sort.by(order1.asOrder()), pageable.getSort());
        assertNull(pageable.next());
        assertNull(pageable.previousOrFirst());
        assertNull(pageable.first());
        assertNull(pageable.withPage(1));
        assertTrue(pageable.hasPrevious());
        assertTrue(pageable.isPaged());
    }

}