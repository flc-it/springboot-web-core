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

package org.flcit.springboot.web.core.converter;

import java.lang.reflect.Field;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonValue;

import org.flcit.commons.core.annotation.EnumConverter;
import org.flcit.commons.core.util.EnumUtils;
import org.flcit.commons.core.util.ReflectionUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class StringToEnumConverterFactory implements ConverterFactory<String, Enum>, ConditionalConverter {

    /**
     *
     */
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        final EnumConverter annotation = targetType.getAnnotation(EnumConverter.class);
        return new StringToEnum(
                targetType,
                annotation.ignoreCase(),
                annotation.nameIfNoMatch(),
                ReflectionUtils.getFirstFieldWithAnnotation(targetType, JsonValue.class));
    }

    /**
     *
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getType().isEnum()
                && targetType.getType().isAnnotationPresent(EnumConverter.class);
    }

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;
        private final boolean ignoreCase;
        private final boolean nameIfNoMatch;
        private final Field field;

        StringToEnum(Class<T> enumType, boolean ignoreCase, boolean nameIfNoMatch, Field field) {
            this.enumType = enumType;
            this.ignoreCase = ignoreCase;
            this.nameIfNoMatch = nameIfNoMatch;
            this.field = field;
        }

        @Override
        @Nullable
        public T convert(String source) {
            if (source.isEmpty()) {
                // It's an empty enum identifier: reset the enum value to null.
                return null;
            }
            if (field == null) {
                return (T) Enum.valueOf(enumType, source);
            }
            try {
                return EnumUtils.convert(source, field, enumType, ignoreCase);
            } catch (IllegalArgumentException e) {
                if (nameIfNoMatch) {
                    return (T) Enum.valueOf(enumType, source);
                }
                throw e;
            }
        }
    }

}
