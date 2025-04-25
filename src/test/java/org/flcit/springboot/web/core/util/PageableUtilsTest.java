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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;

import org.flcit.springboot.commons.core.exception.WrongOrderColumnException;
import org.flcit.commons.core.util.StringUtils;

class PageableUtilsTest {

    @Test
    void filterExpiredTest() {
        final Date current = new Date();
        final Date dateBefore = getDateBefore(current);
        final Date dateAfter = getDateAfter(current);
        assertFalse(PageableUtils.filterExpired(false, dateBefore, dateAfter));
        assertTrue(PageableUtils.filterExpired(false, dateBefore, dateBefore));
        assertTrue(PageableUtils.filterExpired(false, dateAfter, dateAfter));

        assertTrue(PageableUtils.filterExpired(true, dateBefore, dateAfter));
        assertFalse(PageableUtils.filterExpired(true, dateBefore, dateBefore));
        assertFalse(PageableUtils.filterExpired(true, dateAfter, dateAfter));

        assertFalse(PageableUtils.filterExpired(null, null, null));
    }

    @SuppressWarnings("deprecation")
    private static final Date getDateBefore(final Date current) {
        return new Date(current.getYear() - 1, current.getMonth(), current.getDate());
    }

    @SuppressWarnings("deprecation")
    private static final Date getDateAfter(final Date current) {
        return new Date(current.getYear() + 1, current.getMonth(), current.getDate());
    }

    @Test
    void pageTest() {
        final Pageable pageable = PageRequest.ofSize(1);
        final List<Object> list = Arrays.asList(new Object(), new Object());
        final Page<Object> page = PageableUtils.page(list, pageable);
        assertEquals(list.size(), page.getTotalElements());
        assertEquals(pageable.getPageSize(), page.getSize());
        assertEquals(pageable.getPageNumber(), pageable.getPageNumber());
    }

    @Test
    void containsIgnoreCaseTest() {
        assertTrue(PageableUtils.containsIgnoreCase(StringUtils.EMPTY, StringUtils.EMPTY));
        assertTrue(PageableUtils.containsIgnoreCase("test", "test"));
        assertTrue(PageableUtils.containsIgnoreCase("test", "TEST"));
        assertFalse(PageableUtils.containsIgnoreCase("test", StringUtils.EMPTY));
        assertFalse(PageableUtils.containsIgnoreCase("bla", "toto"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void containsDateTest() {
        final Date current = new Date();
        final Date dateBefore = getDateBefore(current);
        final Date dateBefore2 = getDateBefore(current);
        Calendar c = getCalendar(dateBefore);
        c.set(Calendar.DATE, dateBefore.getDate() == 1 ? 2 : 1);
        final Date dateBefore3 = c.getTime();
        c = getCalendar(dateBefore);
        c.set(Calendar.MONTH, dateBefore.getMonth() == 0 ? 1 : 0);
        final Date dateBefore4 = c.getTime();
        final Date dateAfter = getDateAfter(current);
        assertTrue(PageableUtils.containsDate(null, dateBefore));
        assertTrue(PageableUtils.containsDate(dateBefore, dateBefore));
        assertFalse(PageableUtils.containsDate(dateBefore, null));
        assertTrue(PageableUtils.containsDate(dateBefore, dateBefore2));
        assertFalse(PageableUtils.containsDate(dateBefore, dateAfter));
        assertFalse(PageableUtils.containsDate(dateBefore, dateBefore3));
        assertFalse(PageableUtils.containsDate(dateBefore, dateBefore4));
    }

    private static final Calendar getCalendar(Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    @Test
    void compareMethodTest() {
        assertNull(PageableUtils.getCompareMethod("test", CustomObject.class));
        assertNull(PageableUtils.getCompareMethod("test", CustomParentObject.class));
        assertNotNull(PageableUtils.getCompareMethod("id", CustomObject.class));
        assertNotNull(PageableUtils.getCompareMethod("name", CustomObject.class));
        assertNull(PageableUtils.getCompareMethod("enfant", CustomParentObject.class));
        assertNotNull(PageableUtils.getCompareMethod("enumValue", CustomObject.class));
    }

    @Test
    void compareTest() throws ReflectiveOperationException {
        assertNull(PageableUtils.compare(null, null, null, false, null, null));
        Method method = mock(Method.class);
        when(method.getReturnType()).then((i) -> String.class);
        when(method.invoke(isNull())).thenThrow(IllegalAccessException.class);
        assertNull(PageableUtils.compare(method, null, null, false, null, null));

        method = mock(Method.class);
        when(method.getReturnType()).then((i) -> Object.class);
        when(method.invoke(isNull())).thenReturn(null);
        assertNull(PageableUtils.compare(method, null, null, false, null, null));
    }

    @Test
    void compareStringTest() {
        assertEquals(0, PageableUtils.compare("test", "TEST", true, null, null));
        assertEquals(0, PageableUtils.compare(null, null, true, null, null));
    }

    @Test
    void compareComparableTest() {
        assertEquals(-"test".compareTo("TEST"), PageableUtils.compare("test", "TEST", Direction.DESC, null));
    }

    static class CustomObject {
        private final Long id;
        private final String name;
        private final MyEnum enumValue;
        private final Object obj = null;
        private final Date date;
        CustomObject() {
            this(null, null, null, null);
        }
        CustomObject(final Long id, final String name, final MyEnum enumValue, final Date date) {
            this.id = id;
            this.name = name;
            this.enumValue = enumValue;
            this.date = date;
        }
        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public MyEnum getEnumValue() {
            return enumValue;
        }
        public Object getObj() {
            return obj;
        }
        public Date getDate() {
            return date;
        }
    }

    static class CustomParentObject extends CustomObject {
        private final String parent;
        private final CustomObject enfant;
        private final Object fake = null;
        CustomParentObject() {
            this(null, null);
        }
        CustomParentObject(final String parent, final CustomObject enfant) {
            this(null, null, null, null, parent, enfant);
        }
        CustomParentObject(final Long id, final String name, final MyEnum enumValue, final Date date, final String parent, final CustomObject enfant) {
            super(id, name, enumValue, date);
            this.parent = parent;
            this.enfant = enfant;
        }
        public String getParent() {
            return parent;
        }
        public CustomObject getEnfant() {
            return enfant;
        }
        public void getFake() {
        }
        public Object getRealFake() {
            return fake;
        }
    }

    static enum MyEnum {
        B, C;
    }

    static class CustomFilterObject {
        private final Object id;
        private final Object name;
        private final Object parent = null;
        private final Object obj = null;
        private final Object date = null;
        private final Object fake = null;
        CustomFilterObject(final Object id, final Object name) {
            this.id = id;
            this.name = name;
        }
        public Object getId() {
            return id;
        }
        public Object getName() {
            return name;
        }
        public void getParent() {
        }
        public Object getRealParent() {
            return parent;
        }
        public Void getObj() {
            return null;
        }
        public Object getRealObj() {
            return obj;
        }
        public Object getDate(Date date) {
            return date != null ? date : this.date;
        }
        public Object getFake() {
            return fake;
        }
    }

    @Test
    void sortTest() {
        assertNull(PageableUtils.sort(null, null, null, null));
        assertNull(PageableUtils.sort(PageRequest.ofSize(1), null, null, null));
        final Pageable pageable = PageRequest.of(0, 1, Direction.ASC, "test");
        assertThrows(WrongOrderColumnException.class, () -> PageableUtils.sort(pageable, null, CustomObject.class, null));

        final CustomObject obj1 = new CustomObject(1L, "aaaa", MyEnum.B, null);
        final CustomObject obj2 = new CustomObject(1L, "aaaa", MyEnum.B, null);
        final CustomObject obj3 = new CustomObject(2L, "zzzz", MyEnum.C, null);
        final CustomObject objNull = new CustomObject(null, null, null, null);
        final List<CustomObject> list = Arrays.asList(objNull, obj3, obj1, obj2);
        final List<CustomObject> listOrdered = Arrays.asList(obj1, obj2, obj3, objNull);
        final List<CustomObject> listOrderedNullFirst = Arrays.asList(objNull, obj1, obj2, obj3);

        assertIterableEquals(listOrdered, PageableUtils.sort(PageRequest.of(0, 1, Direction.ASC, "name"), list, CustomObject.class, null));
        assertIterableEquals(listOrdered, PageableUtils.sort(PageRequest.of(0, 1, Direction.ASC, "id"), list, CustomObject.class, null));
        assertIterableEquals(listOrdered, PageableUtils.sort(PageRequest.of(0, 1, Direction.ASC, "enumValue"), list, CustomObject.class, null));

        assertIterableEquals(listOrderedNullFirst, PageableUtils.sort(PageRequest.of(0, 1, Sort.by(Order.asc("name").with(NullHandling.NULLS_FIRST))), list, CustomObject.class, null));
        assertIterableEquals(listOrderedNullFirst, PageableUtils.sort(PageRequest.of(0, 1, Sort.by(Order.asc("id").with(NullHandling.NULLS_FIRST))), list, CustomObject.class, null));
        assertIterableEquals(listOrderedNullFirst, PageableUtils.sort(PageRequest.of(0, 1, Sort.by(Order.asc("enumValue").with(NullHandling.NULLS_FIRST))), list, CustomObject.class, null));

        assertIterableEquals(listOrdered, PageableUtils.sort(PageRequest.of(0, 1, Direction.ASC, "custom"), list, CustomObject.class, Collections.singletonMap("custom", (o1, o2) -> {
            if (o1.getName() == null) {
                return 1;
            } else if (o2.getName() == null) {
                return -1;
            } else {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
            }
        })));
    }

    @Test
    void compareExpiredTest() {
        final Date current = new Date();
        final Date dateDebut = getDateBefore(current);
        final Date dateFin = getDateAfter(current);
        assertEquals(1, PageableUtils.compareExpired(dateDebut, dateFin, dateFin, dateFin, null, null));
        assertEquals(-1, PageableUtils.compareExpired(dateFin, dateDebut, dateFin, dateFin, null, null));
        assertEquals(0, PageableUtils.compareExpired(dateDebut, dateFin, dateDebut, dateDebut, null, null));
        assertEquals(0, PageableUtils.compareExpired(dateFin, dateFin, dateFin, dateFin, null, null));
        assertEquals(0, PageableUtils.compareExpired(dateDebut, dateDebut, dateFin, dateFin, null, null));
    }

    @Test
    void filterTest() {
        assertTrue(PageableUtils.filter(null, null));
        assertFalse(PageableUtils.filter(null, new Object()));
        final Date current = new Date();
        final CustomParentObject obj = new CustomParentObject(5L, "name", MyEnum.B, current, "parent", null);
        final CustomParentObject objNull = new CustomParentObject();
        assertFalse(PageableUtils.filter(obj, obj));
        assertTrue(PageableUtils.filter(new CustomParentObject(null, null, null, null, "parent5", null), obj));
        assertTrue(PageableUtils.filter(new CustomParentObject(null, null, MyEnum.C, null, null, null), obj));
        assertTrue(PageableUtils.filter(new CustomParentObject(6L, null, null, null, null, null), obj));
        assertTrue(PageableUtils.filter(new CustomParentObject(null, null, null, getDateBefore(current), null, null), obj));
        assertTrue(PageableUtils.filter(new CustomParentObject(null, "name1", null, null, null, null), obj));

        assertTrue(PageableUtils.filter(new CustomFilterObject(8, "TEST"), objNull));
        assertFalse(PageableUtils.filter(new CustomFilterObject(new Long(5), "name"), obj));
        assertFalse(PageableUtils.filter(new CustomFilterObject(new Long(5), "name5"), obj, "getName"));
    }

}