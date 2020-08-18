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

package com.github.viise.papka.entity;

/**
 * Name of Windows system drive.
 * @see Name
 */
public class NameSystemDriveWin implements Name {

    /**
     * System drive name.
     */
    private final String sysDriveName;

    /**
     * Ctor.
     * @param sysDriveName System drive name. (default - System.getenv("SystemDrive"))
     */
    public NameSystemDriveWin(String sysDriveName) {
        this.sysDriveName = sysDriveName;
    }

    /**
     * Ctor.
     */
    public NameSystemDriveWin() {
        this(System.getenv("SystemDrive"));
    }

    @Override
    public String shortName() {
        return sysDriveName == null ? "C:" : sysDriveName;
    }

    @Override
    public String fullName() {
        return sysDriveName == null ? "C:" : sysDriveName;
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
