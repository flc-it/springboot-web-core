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

import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

/**
 * @param <T>
 * @since 
 * @author Florian Lestic
 */
public class WrapperPageableFilter<T> {

    private T filter;
    private WrapperPageable pageable;
    private String search;

    /**
     * @return
     */
    public T getFilter() {
        return filter;
    }

    /**
     * @param filter
     */
    public void setFilter(T filter) {
        this.filter = filter;
    }

    /**
     * @return
     */
    public Pageable getPageable() {
        return pageable;
    }

    /**
     * @param pageable
     */
    public void setPageable(WrapperPageable pageable) {
        this.pageable = pageable;
    }

    /**
     * @return
     */
    public String getSearch() {
        return search;
    }

    /**
     * @param search
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * @return
     */
    public boolean hasSearch() {
        return StringUtils.hasLength(this.search);
    }
    
    /**
     * @return
     */
    public boolean hasFilter() {
        return this.filter != null;
    }
    
}
