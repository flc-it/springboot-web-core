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

import jakarta.servlet.http.HttpServletResponse;

import org.flcit.commons.core.file.util.ContentTypeUtils;
import org.flcit.commons.core.file.util.FileUtils;
import org.flcit.commons.core.file.util.WebFileUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
public final class ResponseFileUtils {

    private ResponseFileUtils() { }

    /**
     * @param resource
     * @param contentType
     * @return
     */
    public static ResponseEntity<Resource> get(Resource resource, MediaType contentType) {
        return ResponseEntity
                .ok()
                .contentType(contentType)
                .body(resource);
    }

    /**
     * @param resource
     * @param filename
     * @param contentType
     * @return
     */
    public static ResponseEntity<Resource> get(Resource resource, String filename, String contentType) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, WebFileUtils.getContentDisposition(org.springframework.util.StringUtils.hasLength(filename) ? filename : resource.getFilename()))
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    /**
     * @param resource
     * @param newName
     * @return
     */
    public static ResponseEntity<Resource> get(Resource resource, String newName) {
        return get(resource,
                StringUtils.hasLength(newName) ? FileUtils.replaceFilename(resource.getFilename(), newName) : resource.getFilename(),
                        getContentType(resource));
    }

    /**
     * @param response
     * @param filename
     */
    public static void set(HttpServletResponse response, String filename) {
        set(response, filename, ContentTypeUtils.get(filename));
    }

    /**
     * @param response
     * @param filename
     * @param contentType
     */
    public static void set(HttpServletResponse response, String filename, String contentType) {
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, WebFileUtils.getContentDisposition(filename));
        response.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
    }

    private static String getContentType(Resource resource) {
        return ContentTypeUtils.get(resource.getFilename());      
    }

}
