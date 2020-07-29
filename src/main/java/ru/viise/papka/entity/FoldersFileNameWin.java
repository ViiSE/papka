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

import java.util.Arrays;
import java.util.List;

public class FoldersFileNameWin implements FoldersFile<String, String> {

    private final Separator separator;

    public FoldersFileNameWin(Separator separator) {
        this.separator = separator;
    }

    @Override
    public List<String> folders(String fullFilename) {
        String[] foldersName = fullFilename.split(separator.regex());

        int length = foldersName.length - 1;
        if (!(foldersName[0].matches("[A-Z]:"))) { // maybe is internal root? ('/')
            if(foldersName.length == 1) {
                foldersName = addSystemDrive(length, foldersName);
                length = foldersName.length;
            } else if(!(foldersName[1].matches("[A-Z]:"))) {
                if (foldersName[0].equals(""))        // no, system drive is missing, add it...
                    foldersName[0] = new NameSystemDriveWin().fullName();
                else {
                    foldersName = addSystemDrive(length, foldersName);
                    length = foldersName.length;
                }
            }
        }
        String rootName = new NameFolderRoot().fullName();

        if(foldersName[0].equals("")) {
            foldersName[0] = rootName;
        }

        if(fullFilename.charAt(fullFilename.length() - 1) == separator.charS())
            ++length;
        if(!(foldersName[0].equals(rootName))) {
            foldersName = pushRoot(rootName, foldersName, length);
            length = foldersName.length;
        }

        return Arrays.asList(Arrays.copyOfRange(foldersName, 0, length));
    }

    private String[] pushRoot(String rootName, String[] folders, int length) {
        String[] foldersWithRoot = new String[length + 1];
        foldersWithRoot[0] = rootName;
        System.arraycopy(folders, 0, foldersWithRoot, 1, length);
        return foldersWithRoot;
    }

    private String[] addSystemDrive(int length, String[] foldersName) {
        ++length;
        return pushRoot(new NameSystemDriveWin().fullName(), foldersName, length - 1);
    }
}
