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

import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.find.Find;

import java.util.ArrayList;
import java.util.List;

public class FilterFilesRaw implements Filter<List<String>> {

    private final Find<List<String>, List<String>> find;
    private final List<String> rawFiles;

    public FilterFilesRaw(Find<List<String>, List<String>> find, List<String> rawFiles) {
        this.find = find;
        this.rawFiles = rawFiles;
    }

    @Override
    public List<String> apply() {
        try {
            return find.answer(rawFiles);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
