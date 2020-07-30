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

package ru.viise.papka.search;

import ru.viise.papka.entity.*;
import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.system.Os;
import ru.viise.papka.system.OsRepo;
import ru.viise.papka.system.OsRepository;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Search folder by name in system.
 * Regex supported.
 * @see ru.viise.papka.search.SearchFolder
 */
public class SearchFolderInSystem implements SearchFolder<File, String> {

    /**
     * {@link OsRepository}.
     */
    private final OsRepository osRepo;

    /**
     * Name of the folder to start the search from.
     */
    private final String beginWith;

    /**
     * Ctor.
     * @param osRepo {@link OsRepository}.
     * @param beginWith Name of the folder to start the search from.
     */
    public SearchFolderInSystem(OsRepository osRepo, String beginWith) {
        this.osRepo = osRepo;
        this.beginWith = beginWith;
    }

    /**
     * Ctor.
     * @param beginWith Name of the folder to start the search from.
     */
    public SearchFolderInSystem(String beginWith) {
        this(new OsRepo(), beginWith);
    }

    @Override
    public Folder<File> answer(String regex) throws NotFoundException {
        Search<List<File>, String> searchFiles = new SearchFilesByFolderNameInSystem(beginWith);
        List<File> files = searchFiles.answer(regex);

        Os os = osRepo.instance();

        PreparedFolders<Map<String, Folder<File>>> preparedFolders = new PreparedFoldersMapFileSystem(
                new PreparedFoldersMapFilesRaw(
                        os.foldersFile(),
                        os.separator(),
                        files));

        TreeFolder<File> treeFolder = new TreeFolderPure<>(
                os.foldersFile(),
                preparedFolders);

        Folder<File> findFolder = treeFolder.grow();

        AtomicReference<Folder<File>> result = new AtomicReference<>();
        AtomicReference<Boolean> isEnough = new AtomicReference<>(false);

        findFolder.travel(folder -> {
            if(!isEnough.get()) {
                if (folder.fullName().matches(regex)) {
                    result.set(folder);
                    isEnough.set(true);
                }
            }
        });

        return result.get();
    }
}
