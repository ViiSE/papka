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

import com.github.viise.papka.system.SeparatorWin;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;

public class PreparedFoldersMapRawTestNG {

    private List<List<String>> files;
    private PreparedFolders<Map<String, List<String>>> prepFolders;

    @BeforeClass
    public void beforeClass() {
        files = new ArrayList<>();

        List<String> root = new ArrayList<>();
        root.add("root1.txt");
        root.add("root2.txt");
        root.add("root3.txt");
        root.add("file1.txt");
        root.add("file2.txt");
        root.sort(String::compareTo);

        List<String> folder1 = new ArrayList<>();
        folder1.add("file1.txt");
        folder1.add("file2.txt");
        folder1.sort(String::compareTo);

        List<String> folder2 = new ArrayList<>();
        folder2.add("file1.txt");
        folder2.sort(String::compareTo);

        files.add(root);
        files.add(folder1);
        files.add(folder2);

        List<String> rawFiles = new ArrayList<>();
        rawFiles.add("root1.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root3.txt");
        rawFiles.add("/file1.txt");
        rawFiles.add("/file2.txt");
        rawFiles.add("/folder1/file1.txt");
        rawFiles.add("/folder1/file2.txt");
        rawFiles.add("/folder2/file1.txt");
        rawFiles.sort(String::compareTo);

        prepFolders = new PreparedFoldersMapRaw(rawFiles);
    }

    @Test
    public void ctor_list() {
        new PreparedFoldersMapRaw(new ArrayList<>());
    }

    @Test
    public void ctor_FoldersFileAndRaw() {
        new PreparedFoldersMapRaw(
                new FoldersFileName(),
                new ArrayList<>());
    }

    @Test
    public void preparation() {
        Map<String, List<String>> mapRaw = prepFolders.preparation();
        AtomicInteger i = new AtomicInteger();
        mapRaw.forEach((fullFolderName, prepFiles) ->
                assertEquals(prepFiles, files.get(i.getAndIncrement())));
    }

    @Test
    public void preparation_win() {
        List<String> rawFiles = new ArrayList<>();
        rawFiles.add("root1.txt");
        rawFiles.add("C:\\root2.txt");
        rawFiles.add("C:\\root3.txt");
        rawFiles.add("C:\\file1.txt");
        rawFiles.add("C:\\file2.txt");
        rawFiles.add("C:\\folder1\\file1.txt");
        rawFiles.add("C:\\folder1\\file2.txt");
        rawFiles.add("C:\\folder2\\file1.txt");
        rawFiles.sort(String::compareTo);

        prepFolders = new PreparedFoldersMapRaw(new FoldersFileNameWin(new SeparatorWin()), new SeparatorWin(), rawFiles);

        Map<String, List<String>> mapRaw = prepFolders.preparation();
        AtomicInteger i = new AtomicInteger();
        mapRaw.forEach((fullFolderName, prepFiles) -> {
            if(!fullFolderName.equals("/"))
                assertEquals(prepFiles, files.get(i.getAndIncrement()));
        });
    }
}
