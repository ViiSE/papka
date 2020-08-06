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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;

public class PreparedFoldersMapTextTestNG {

    private List<Folder<String>> folders;
    private PreparedFolders<Map<String, Folder<String>>> prepFolders;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.txt");
        rootFiles.add("root2.txt");
        rootFiles.add("root3.txt");

        List<String> folder1Files = new ArrayList<>();
        folder1Files.add("file1.txt");
        folder1Files.add("file2.txt");

        List<String> folder2Files = new ArrayList<>();
        folder2Files.add("file1.txt");

        List<String> rawFiles = new ArrayList<>();
        rawFiles.add("/root1.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root3.txt");
        rawFiles.add("/folder1/file1.txt");
        rawFiles.add("/folder1/file2.txt");
        rawFiles.add("/folder2/file1.txt");

        folders = new ArrayList<>();
        folders.add(new FolderPure<>(
                        new NameFolderRoot(),
                        rootFiles));
        folders.add(new FolderPure<>(
                "/folder1/",
                        folder1Files));
        folders.add(new FolderPure<>(
                "/folder2/",
                        folder2Files));

        prepFolders = new PreparedFoldersMapText(new PreparedFoldersMapRaw(rawFiles));
    }

    @Test
    public void preparation() {
        Map<String, Folder<String>> mapFolder = prepFolders.preparation();
        AtomicInteger i = new AtomicInteger();
        mapFolder.forEach((fullName, folder) ->
                assertEquals(folder, folders.get(i.getAndIncrement())));
    }
}
