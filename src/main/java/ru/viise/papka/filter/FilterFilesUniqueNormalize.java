/*
 * Copyright 2020 ViiSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.papka.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterFilesUniqueNormalize<T> implements Filter<List<T>> {

    private final List<T> rawFiles;

    public FilterFilesUniqueNormalize(List<T> rawFiles) {
        this.rawFiles = rawFiles;
    }

    @Override
    public List<T> apply() {
        List<T> readyFiles = new ArrayList<>();
        for(int i = 0; i < rawFiles.size(); i++) {
            T file = rawFiles.get(i);
            boolean isAdd = true;
            for(int j = 0; j < rawFiles.size(); j++) {
                if(i != j)
                    if(file.equals(rawFiles.get(j)))
                        if(readyFiles.contains(file)) {
                            isAdd = false;
                        }
            }

            if(isAdd)
                readyFiles.add(file);
        }

        return readyFiles;
    }
}
