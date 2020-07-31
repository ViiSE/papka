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

/**
 * Pure implementation of Name interface.
 * @see ru.viise.papka.entity.Name
 */
public class NamePure implements Name {

    /**
     * Pure full name.
     */
    private String pureFullName;

    /**
     * Pure short name;
     */
    private String pureShortName;

    /**
     * Raw full name.
     */
    private final String fullName;

    /**
     * {@link Separator}.
     */
    private final Separator separator;

    /**
     * Ctor.
     *
     * @param fullName Raw full name.
     */
    public NamePure(String fullName) {
        this(fullName, new SeparatorUnix());
    }

    /**
     * Ctor.
     *
     * @param fullName  Raw full name.
     * @param separator {@link Separator}.
     */
    public NamePure(String fullName, Separator separator) {
        this.fullName = fullName;
        this.separator = separator;
    }

    @Override
    public String shortName() {
        if (pureShortName == null)
            fullInit();

        return pureShortName;
    }

    @Override
    public String fullName() {
        if (pureFullName == null)
            fullInit();

        return pureFullName;
    }

    private String pure(String name) {
        return name
                .replace("*", "")
                .replace("?", "")
                .replace("\"", "")
                .replace("<", "")
                .replace(">", "")
                .replace("|", "")
                .replace("+", "")
                .replace("\u0000", "");
    }

    public String init() {
        String name = fullName.replace("//", "/"); // for root folder

        if (name.length() == 1)
            return name;

        if (name.charAt(name.length() - 1) == separator.charS())
            name = name.substring(0, name.length() - 1);
        if (name.charAt(name.length() - 1) == separator.mirror().charS())
            name = name.substring(0, name.length() - 1);
        name = name.charAt(0) + name.substring(1).replace(separator.mirror().pure(), separator.pure());
        return name;
    }

    public void fullInit() {
        String pureName = init();
        String[] names;

        if (pureName.length() == 1) {
            pureShortName = pureName;
            pureFullName = pureName;
        } else {
            names = pureName.split(separator.regex());
            pureShortName = pure(names[names.length - 1]);
            pureFullName = pure(pureName);
        }
    }

    @Override
    public boolean equals(Object name) {
        if(!(name instanceof Name))
            return false;

        if(((Name) name).fullName().equals(fullName()))
                return true;

        return super.equals(name);
    }
}
