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

package org.flcit.springboot.web.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.flcit.springboot.web.core.domain.WrapperBootstrapTable;

class ResponseEntityUtilsTest {

    @Test
    void test() {
        assertEquals(HttpStatus.NO_CONTENT, ResponseEntityUtils.noContent().getStatusCode());
        final Object object = new Object();
        ResponseEntity<Object> response = ResponseEntityUtils.object(object);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(object, response.getBody());
        response = ResponseEntityUtils.object(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void bootstrapTableTest() {
        final long total = 100;
        final Pageable pageable = PageRequest.ofSize(1);
        final List<Object> list = Collections.singletonList(new Object());
        ResponseEntity<WrapperBootstrapTable<Object>> responseTable = ResponseEntityUtils.bootstrapTable(new PageImpl<>(list, pageable, total));
        assertEquals(HttpStatus.OK, responseTable.getStatusCode());
        assertEquals(total, responseTable.getBody().getTotal());
        assertEquals(list, responseTable.getBody().getRows());
        responseTable = ResponseEntityUtils.bootstrapTable(null);
        assertEquals(HttpStatus.NOT_FOUND, responseTable.getStatusCode());
        assertNull(responseTable.getBody());
    }

}