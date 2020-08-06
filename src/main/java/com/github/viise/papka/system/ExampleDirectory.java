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

package com.github.viise.papka.system;

/**
 * Example directory in project.
 * @see Directory
 */
public class ExampleDirectory implements Directory<String> {

    /**
     * Current {@link Directory}.
     */
    private final Directory<String> curDir;

    /**
     * {@link Separator}.
     */
    private final Separator separator;

    /**
     * Ctor.
     * @param curDir Current {@link Directory}.
     * @param separator {@link Separator}.
     */
    public ExampleDirectory(Directory<String> curDir, Separator separator) {
        this.curDir = curDir;
        this.separator = separator;
    }

    @Override
    public String name() {
        return (curDir.name() + "example" + separator.pure())
                .replace(separator.mirror().pure(), separator.pure());
    }
}
