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

package org.flcit.springboot.web.core.file.util;

import org.springframework.http.MediaType;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
public final class MediaTypeExtend {

    private MediaTypeExtend() { }

    private static final String APPLICATION = "application";

    public static final MediaType APPLICATION_GZIP = new MediaType(APPLICATION, "gzip");
    public static final MediaType APPLICATION_ZIP = new MediaType(APPLICATION, "zip");
    public static final MediaType APPLICATION_SQL = new MediaType(APPLICATION, "sql");
    public static final MediaType APPLICATION_OPENDOCUMENT_ODT = new MediaType(APPLICATION, "vnd.oasis.opendocument.text");
    public static final MediaType APPLICATION_WORD_DOC = new MediaType(APPLICATION, "msword");
    public static final MediaType APPLICATION_WORD_DOCX = new MediaType(APPLICATION, "vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MediaType TEXT_CSV = new MediaType("text", "csv");

}
