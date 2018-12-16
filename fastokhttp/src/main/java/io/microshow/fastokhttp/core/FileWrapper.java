/*
 * Copyright (C) 2017 zhichao.huang, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.microshow.fastokhttp.core;

import okhttp3.MediaType;
import java.io.File;

/**
 * FileWrapper
 * @author zhichao.huang
 *
 */
public class FileWrapper {
    public File file;
    public String fileName;
    public MediaType mediaType;
    private long fileSize;

    public FileWrapper(File file, MediaType mediaType) {
        this.file = file;
        this.fileName = file.getName();
        this.mediaType = mediaType;
        this.fileSize = file.length();
    }

    public String getFileName() {
        if (fileName != null) {
            return fileName;
        } else {
            return "nofilename";
        }
    }

    public File getFile() {
        return file;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }
}
