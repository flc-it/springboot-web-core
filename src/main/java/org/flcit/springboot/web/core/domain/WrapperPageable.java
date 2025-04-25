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

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.ObjectUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class WrapperPageable implements Pageable {

    private int pageNumber;
    private int pageSize;
    private WrapperOrder[] orders;

    /**
     *
     */
    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     *
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     *
     */
    @Override
    public long getOffset() {
        return (long) pageNumber * pageSize;
    }

    /**
     * @param orders
     */
    public void setOrders(WrapperOrder[] orders) {
        this.orders = orders;
    }

    /**
     *
     */
    @Override
    public Sort getSort() {
        if (ObjectUtils.isEmpty(orders)) {
            return Sort.unsorted();
        }
        final List<Order> orders1 = new ArrayList<>(this.orders.length);
        for (WrapperOrder order : this.orders) {
            if (order.isValid()) {
                orders1.add(order.asOrder());
            }
        }
        return Sort.by(orders1);
    }

    /**
     *
     */
    @Override
    public Pageable next() {
        return null;
    }

    /**
     *
     */
    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    /**
     *
     */
    @Override
    public Pageable first() {
        return null;
    }

    /**
     *
     */
    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    /**
     *
     */
    @Override
    public boolean hasPrevious() {
        return getOffset() > 0;
    }

    /**
     *
     */
    @Override
    public boolean isPaged() {
        return pageSize > 0 ;
    }

}
