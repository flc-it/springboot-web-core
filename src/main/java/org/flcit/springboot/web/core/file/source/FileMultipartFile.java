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

package org.flcit.springboot.web.core.file.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.flcit.commons.core.file.util.ContentTypeUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @since 1.0.0
 * @author Florian Lestic
 */
public class FileMultipartFile implements MultipartFile {

    private final File file;

    /**
     * @param file
     */
    public FileMultipartFile(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return this.getName();
    }

    @Override
    public String getContentType() {
        return ContentTypeUtils.get(this.getName());
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public long getSize() {
        return this.file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(this.file.toPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(this.file.toPath());
    }

    @Override
    public void transferTo(File dest) throws IOException {
        Files.copy(this.file.toPath(), dest.toPath());
    }

}
