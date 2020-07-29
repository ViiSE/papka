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

public class FilterFiles<T> implements Filter<List<T>> {

    private final Find<List<T>, String> find;
    private final String regex;

    public FilterFiles(Find<List<T>, String> find, String regex) {
        this.find = find;
        this.regex = regex;
    }

    @Override
    public List<T> apply() {
        try {
            return find.answer(regex);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
