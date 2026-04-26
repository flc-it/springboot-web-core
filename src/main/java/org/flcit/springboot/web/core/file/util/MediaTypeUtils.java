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
import org.springframework.util.StringUtils;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
public final class MediaTypeUtils {

    private MediaTypeUtils() { }

    /**
     * @param filename
     * @return
     */
    public static MediaType getByFilename(String filename) {
        final String extension = StringUtils.getFilenameExtension(filename);
        if (!StringUtils.hasLength(extension)) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        switch (extension) {
            case "pdf": return MediaType.APPLICATION_PDF;
            case "json": return MediaType.APPLICATION_JSON;
            case "doc": return MediaTypeExtend.APPLICATION_WORD_DOC;
            case "docx": return MediaTypeExtend.APPLICATION_WORD_DOCX;
            case "zip": return MediaTypeExtend.APPLICATION_ZIP;
            case "odt": return MediaTypeExtend.APPLICATION_OPENDOCUMENT_ODT;
            case "txt": return MediaType.TEXT_PLAIN;
            case "htm": return MediaType.TEXT_HTML;
            case "html": return MediaType.TEXT_HTML;
            case "xml": return MediaType.TEXT_XML;
            case "csv": return MediaTypeExtend.TEXT_CSV;
            case "md": return MediaType.TEXT_MARKDOWN;
            case "png": return MediaType.IMAGE_PNG;
            case "jpg": return MediaType.IMAGE_JPEG;
            case "jpeg": return MediaType.IMAGE_JPEG;
            case "gif": return MediaType.IMAGE_GIF;
            default: return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
