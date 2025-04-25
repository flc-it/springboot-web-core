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

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.util.StringUtils;

import org.flcit.springboot.commons.core.exception.WrongOrderColumnException;
import org.flcit.commons.core.util.ObjectUtils;
import org.flcit.commons.core.util.ReflectionUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class PageableUtils {

    private PageableUtils() { }

    /**
     * @param test
     * @param value
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean containsDate(Date test, Date value) {
        if (test == null
                || test == value) {
            return true;
        }
        if (value == null) {
            return false;
        }
        return test.getYear() == value.getYear()
                && test.getMonth() == value.getMonth()
                && test.getDate() ==  value.getDate();
    }

    private static boolean equals(Object test, Object value) {
        if (test == null
                || test == value) {
            return true;
        }
        if (value == null) {
            return false;
        }
        return test.equals(value);
    }

    /**
     * @param test
     * @param value
     * @return
     */
    public static boolean containsIgnoreCase(String test, String value) {
        if (!StringUtils.hasLength(test)
                || test.equals(value)) {
            return true;
        }
        if (!StringUtils.hasLength(value)) {
            return false;
        }
        return value.toLowerCase().contains(test.toLowerCase());
    }

    private static boolean expired(Date dateDebut, Date dateFin) {
        final Date current = new Date();
        return current.before(dateDebut) || current.after(dateFin);
    }

    private static String toString(final Object value) {
        return value == null ? null : value.toString();
    }

    /**
     * @param value1
     * @param value2
     * @param ignoreCase
     * @param direction
     * @param nullHandling
     * @return
     */
    public static int compare(final String value1, final String value2, final boolean ignoreCase, final Direction direction, final NullHandling nullHandling) {
        if ((value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))) {
            return 0;
        }
        if (value1 == null) {
            return compareNull1(nullHandling, direction);
        } else if (value2 == null) {
            return compareNull2(nullHandling, direction);
        } else {
            return compare(ignoreCase ? value1.compareToIgnoreCase(value2) : value1.compareTo(value2), direction);
        }
    }

    /**
     * @param <T>
     * @param value1
     * @param value2
     * @param direction
     * @param nullHandling
     * @return
     */
    public static <T> int compare(final Comparable<T> value1, final T value2, final Direction direction, final NullHandling nullHandling) {
        if (value1 == value2) {
            return 0;
        } else if (value1 == null) {
            return compareNull1(nullHandling, direction);
        } else if (value2 == null) {
            return compareNull2(nullHandling, direction);
        } else {
            return compare(value1.compareTo(value2), direction);
        }
    }

    private static int compareNull1(NullHandling nullHandling, Direction direction) {
        return compare(nullHandling == NullHandling.NULLS_FIRST ? -1 : 1, direction);
    }

    private static int compareNull2(NullHandling nullHandling, Direction direction) {
        return compare(nullHandling == NullHandling.NULLS_FIRST ? 1 : -1, direction);
    }

    private static int compare(int compare, Direction direction) {
        return direction == null || direction.isAscending() ? compare : -compare;
    }

    /**
     * @param dateDebut1
     * @param dateDebut2
     * @param dateFin1
     * @param dateFin2
     * @param direction
     * @param nullHandling
     * @return
     */
    public static int compareExpired(Date dateDebut1, Date dateDebut2, Date dateFin1, Date dateFin2, Direction direction, NullHandling nullHandling) {
        final boolean expired1 = expired(dateDebut1, dateFin1);
        final boolean expired2 = expired(dateDebut2, dateFin2);
        if (expired1 && !expired2) {
            return compare(-1, direction);
        } else if (!expired1 && expired2) {
            return compare(1, direction);
        } else {
            return compare(dateFin1, dateFin2, direction, nullHandling);
        }
    }

    /**
     * @param expired
     * @param dateDebut
     * @param dateFin
     * @return
     */
    public static boolean filterExpired(Boolean expired, Date dateDebut, Date dateFin) {
        if (expired == null) {
            return false;
        }
        final Date date = new Date();
        return (Boolean.TRUE.equals(expired)
                && dateDebut.compareTo(date) <= 0
                && dateFin.compareTo(date) >= 0)
                || (Boolean.FALSE.equals(expired)
                        && (dateDebut.compareTo(date) > 0
                                || dateFin.compareTo(date) < 0));
    }

    /**
     * @param <T>
     * @param list
     * @param pageable
     * @return
     */
    public static <T> Page<T> page(List<T> list, Pageable pageable) {
        final int begin = (int) pageable.getOffset();
        return new PageImpl<>(list.subList(begin, Math.min(list.size(), begin + pageable.getPageSize())), pageable, list.size());
    }

    /**
     * @param filter
     * @param value
     * @param methodsToExclude
     * @return
     */
    public static boolean filter(Object filter, Object value, String... methodsToExclude) {
        if (value == null) {
            return true;
        }
        if (filter == null) {
            return false;
        }
        return filter(filter.getClass(), filter, value, methodsToExclude);
    }

    private static boolean filter(Class<?> clazz, Object filter, Object value, String... methodsToExclude) {
        for (Method methodFilter : clazz.getDeclaredMethods()) {
            if (!org.springframework.util.ObjectUtils.containsElement(methodsToExclude, methodFilter.getName())
                    && filterValue(methodFilter, filter, value.getClass(), value)) {
                return true;
            }
        }
        return ReflectionUtils.hasSuperClass(clazz)
                && filter(clazz.getSuperclass(), filter, value);
    }

    private static boolean filterValue(Method method, Object filter, Class<?> clazz, Object value) {
        try {
            return filterValue(method, filter, clazz.getDeclaredMethod(method.getName()), value);
        } catch (ReflectiveOperationException e) {
            return ReflectionUtils.hasSuperClass(clazz) && filterValue(method, filter, clazz.getSuperclass(), value);
        }
    }

    private static boolean isFilterMethod(Method method) {
        if (method.getParameterCount() > 0) {
            return false;
        }
        final Class<?> responseClass = method.getReturnType();
        return responseClass != void.class
                && responseClass != Void.class;
    }

    private static boolean filterValue(Method methodFilter, Object filter, Method methodValue, Object value) throws ReflectiveOperationException {
        if (!isFilterMethod(methodFilter)
                || !isFilterMethod(methodValue)) {
            return false;
        }
        if (!methodValue.getReturnType().equals(methodFilter.getReturnType())) {
            return !equals(methodFilter.invoke(filter), methodValue.invoke(value));
        } else {
            switch (methodFilter.getReturnType().getName()) {
            case "java.lang.String":
                return !containsIgnoreCase((String) methodFilter.invoke(filter), (String) methodValue.invoke(value));
            case "java.util.Date":
                return !containsDate((Date) methodFilter.invoke(filter), (Date) methodValue.invoke(value));
            default:
                return !equals(methodFilter.invoke(filter), methodValue.invoke(value));
            }
        }
    }

    /**
     * @param property
     * @param clazz
     * @return
     */
    public static Method getCompareMethod(String property, Class<?> clazz) {
        final Method method = ReflectionUtils.getterMethod(clazz, property);
        return method == null || isCompareMethod(method) ? method : null;
    }

    private static boolean isCompareMethod(Method method) {
        final Class<?> responseClass = method.getReturnType();
        return String.class.isAssignableFrom(responseClass)
                || responseClass.isEnum()
                || Comparable.class.isAssignableFrom(responseClass);
    }

    /**
     * @param <E>
     * @param method
     * @param entry1
     * @param entry2
     * @param ignoreCase
     * @param direction
     * @param nullHandling
     * @return
     */
    public static <E> Integer compare(Method method, E entry1, E entry2,
            boolean ignoreCase, Direction direction, NullHandling nullHandling) {
        if (method == null) {
            return null;
        }
        try {
            if (String.class.isAssignableFrom(method.getReturnType())) {
                return compare((String) method.invoke(entry1), (String) method.invoke(entry2), ignoreCase, direction, nullHandling);
            } else {
                return compareValue(method.getReturnType(), method.invoke(entry1), method.invoke(entry2), ignoreCase, direction, nullHandling);
            }
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Integer compareValue(Class<?> clazz, T entry1, T entry2, boolean ignoreCase, Direction direction, NullHandling nullHandling) {
        if (clazz.isEnum()) {
            return compare(toString(entry1), toString(entry2), ignoreCase, direction, nullHandling);
        } else if (Comparable.class.isAssignableFrom(clazz)) {
            return compare((Comparable<T>) entry1, entry2, direction, nullHandling);
        } else {
            return null;
        }
    }

    /**
     * @param <T>
     * @param pageable
     * @param list
     * @param clazz
     * @param customComparators
     * @return
     */
    public static <T> List<T> sort(Pageable pageable, List<T> list, Class<T> clazz, Map<String, Comparator<T>> customComparators) {
        if (pageable == null
                || pageable.getSort().isUnsorted()) {
            return list;
        }
        final String property = pageable.getSort().toList().get(0).getProperty();
        final Direction direction = pageable.getSort().toList().get(0).getDirection();
        final boolean ignoreCase = pageable.getSort().toList().get(0).isIgnoreCase();
        final NullHandling nullHandling = pageable.getSort().toList().get(0).getNullHandling();
        final Comparator<T> customComparator = customComparators != null ? customComparators.get(property) : null;
        final Method method = customComparator == null ? getCompareMethod(property, clazz) : null;
        if (customComparator == null
                && method == null) {
            throw new WrongOrderColumnException(property);
        }
        if (customComparator != null) {
            list.sort(customComparator);
        } else {
            list.sort((v1, v2) -> ObjectUtils.getOrDefault(compare(method, v1, v2, ignoreCase, direction, nullHandling), 0));
        }
        return list;
    }

}
