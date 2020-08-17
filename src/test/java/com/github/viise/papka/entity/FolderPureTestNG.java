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
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class FolderPureTestNG {

    private Folder<String> root;
    private List<String> rootFiles;
    private Folder<String> child;

    @BeforeClass
    public void beforeClass() {
        rootFiles = new ArrayList<String>() {{
            add("file1.png");
            add("file2.png");
        }};

        child = new FolderPure<>(
                "/music",
                new ArrayList<String>() {{
                    add("musicA.mp3"); }});

        root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new ArrayList<Folder<String>>() {{
                    add(child);
                }});
    }

    @Test
    public void ctor_rawNameAndListFiles() {
        new FolderPure<>(
                "/folder",
                new ArrayList<>());
    }

    @Test
    public void ctor_rawNameAndVarargsFiles() {
        new FolderPure<>(
                "/folder",
                "file1",
                "file2");
    }

    @Test
    public void ctor_rawNameListFilesAndVarargsChildren() {
        new FolderPure<>(
                "/folder",
                new ArrayList<String>() {{
                    add("file1");
                    add("file2");
                }},
                new FolderPure<>(
                        "/child1",
                        new ArrayList<>()),
                new FolderPure<>(
                        "child2",
                        new ArrayList<>()));
    }

    @Test
    public void ctor_rawNameListFilesAndListChildren() {
        new FolderPure<>(
                "/folder",
                new ArrayList<String>() {{
                    add("file1");
                    add("file2");
                }},
                new ArrayList<Folder<String>>() {{
                    add(new FolderPure<>(
                            "/child1",
                            new ArrayList<>()));
                    new FolderPure<>(
                            "child2",
                            new ArrayList<>());
                }});
    }

    @Test
    public void ctor_nameAndVarargsFiles() {
        new FolderPure<>(
                new NamePure("/folder"),
                "file1");
    }

    @Test
    public void ctor_nameAndListFiles() {
        new FolderPure<>(
                new NamePure("/folder"),
                new ArrayList<>());
    }

    @Test
    public void ctor_nameListFilesAndVarargsChildren() {
        new FolderPure<>(
                new NamePure("/folder"),
                new ArrayList<String>() {{
                    add("file1");
                    add("file2");
                }},
                new FolderPure<>(
                        "/child1",
                        new ArrayList<>()),
                new FolderPure<>(
                        "child2",
                        new ArrayList<>()));
    }

    @Test
    public void ctor_nameListFilesAndListChildren() {
        new FolderPure<>(
                new NamePure("/folder"),
                new ArrayList<String>() {{
                    add("file1");
                    add("file2");
                }},
                new ArrayList<Folder<String>>() {{
                    add(new FolderPure<>(
                            "/child1",
                            new ArrayList<>()));
                    new FolderPure<>(
                            "child2",
                            new ArrayList<>());
                }});
    }

    @Test
    public void files() {
        List<String> files = root.files();
        assertEquals(files, rootFiles);
    }

    @Test
    public void children() {
        List<Folder<String>> children = root.children();
        assertEquals(children.get(0), child);
    }

    @Test
    public void fullName() {
        assertEquals("/", root.fullName());
    }

    @Test
    public void shortName() {
        assertEquals("/", root.shortName());
    }

    @Test
    public void travel() {
        AtomicInteger i = new AtomicInteger();
        List<Folder<String>> folders = new ArrayList<>();
        folders.add(root);
        folders.add(child);

        root.travel(folder -> assertEquals(folder, folders.get(i.getAndIncrement())));
    }

    @Test
    public void eq() {
        List<String> rootFiles = new ArrayList<String>() {{
            add("file1.png");
            add("file2.png");
        }};

        Folder<String> child = new FolderPure<>(
                "/music",
                new ArrayList<String>() {{
                    add("musicA.mp3"); }});

        Folder<String> expected = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new ArrayList<Folder<String>>() {{
                    add(child);
                }});

        assertEquals(expected, root);
    }

    @Test
    public void eq_wrongType() {
        Folder<String> actual = new FolderPure<>(
                new NameFolderRoot(),
                new ArrayList<>()
        );

        String fileFake = "file.png";
        assertNotEquals(fileFake, actual);
    }

    @Test
    public void eq_no() {
        List<String> rootFiles = new ArrayList<String>() {{
            add("file1.png");
            add("file2.png");
            add("file3.png");
        }};

        Folder<String> child = new FolderPure<>(
                "/music",
                new ArrayList<String>() {{
                    add("musicA.mp3"); }});

        Folder<String> expected = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new ArrayList<Folder<String>>() {{
                    add(child);
                }});

        assertNotEquals(expected, root);
    }
}
