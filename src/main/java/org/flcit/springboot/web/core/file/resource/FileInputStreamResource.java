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

package org.flcit.springboot.web.core.file.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
public class FileInputStreamResource extends InputStreamResource {

    private final String filename;
    private final long contentLength;
    private final MediaType contentType;

    /**
     * @param inputStream
     * @param filename
     * @param contentLength
     */
    public FileInputStreamResource(InputStream inputStream, String filename, long contentLength) {
        this(inputStream, filename, null, contentLength);
    }

    /**
     * @param inputStream
     * @param filename
     * @param contentType
     * @param contentLength
     */
    public FileInputStreamResource(InputStream inputStream, String filename, MediaType contentType, long contentLength) {
        super(inputStream);
        this.filename = filename;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return contentLength;
    }

    /**
     * @return
     */
    public MediaType getContentType() {
        return contentType;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(contentLength, contentType, filename);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileInputStreamResource other = (FileInputStreamResource) obj;
        return contentLength == other.contentLength && Objects.equals(contentType, other.contentType)
                && Objects.equals(filename, other.filename);
    }

}
