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

package ru.viise.papka.entity;

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;

import java.util.List;

public class NameFoldersPath implements Name {

    private String fullName = "";
    private String shortName = "";
    private final Separator separator;
    private final List<String> foldersName;

    public NameFoldersPath(List<String> foldersName, Separator separator) {
        this.separator = separator;
        this.foldersName = foldersName;
    }

    public NameFoldersPath(List<String> foldersName) {
        this(foldersName, new SeparatorUnix());
    }

    @Override
    public String shortName() {
        if(shortName.isEmpty())
            shortName = _shortName();
        return shortName;
    }

    @Override
    public String fullName() {
        if(fullName.isEmpty())
            fullName = _fullName();
        return fullName;
    }

    private String _fullName() {
        String fullName = String.join(separator.pure(), foldersName) + separator.pure();
        return fullName.replace(separator.pure() + separator.pure(), separator.pure());
    }

    private String _shortName() {
        return foldersName.get(foldersName.size() - 1);
    }
}