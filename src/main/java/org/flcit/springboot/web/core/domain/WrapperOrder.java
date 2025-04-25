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

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class WrapperOrder {

    private Direction direction;
    private String name;

    /**
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public boolean isValid() {
        return direction != null && StringUtils.hasLength(name);
    }

    /**
     * @return
     */
    public Order asOrder() {
        return isValid() ? new Order(direction, name) : null;
    }

}
