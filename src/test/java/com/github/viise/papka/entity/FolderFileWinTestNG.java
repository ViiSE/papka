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

import com.github.viise.papka.exception.NotFoundException;
import com.github.viise.papka.search.SearchByStartWith;
import com.github.viise.papka.search.SearchFoldersByRegex;
import com.github.viise.papka.system.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class FolderFileWinTestNG {

    private boolean isWindows;

    @BeforeClass
    public void beforeClass() {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    }

    @Test
    public void ctor_withNameAndVarargs() {
        new FolderFileWin(
                new NameFolderRoot(),
                "/home/text.txt");
    }

    @Test
    public void ctor_withExNonExistAndVarargs() {
        new FolderFileWin(
                true,
                "/home/text.txt");
    }

    @Test
    public void ctor_withNameAndExNonExistAndVarargs() {
        new FolderFileWin(
                new NameFolderRoot(),
                true,
                "/home/text.txt");
    }

    @Test
    public void ctor_withNameAndExNonExistAndIsUnixLikeAndVarargs() {
        new FolderFileWin(
                new NameFolderRoot(),
                true,
                true,
                "/home/text.txt");
    }

    @Test
    public void ctor_withList() {
        new FolderFileWin(
                new ArrayList<String>(){{
                    add("/home/text.txt");
                }});
    }

    @Test
    public void ctor_withNameAndList() {
        new FolderFileWin(
                new NameFolderRoot(),
                new ArrayList<String>(){{
                    add("/home/text.txt");
                }});
    }

    @Test
    public void ctor_withListAndExNonExist() {
        new FolderFileWin(
                new ArrayList<String>(){{
                    add("/home/text.txt");
                }},
                true);
    }

    @Test
    public void ctor_withListAndExNonExistAndIsUnixLike() {
        new FolderFileWin(
                new ArrayList<String>(){{
                    add("/home/text.txt");
                }},
                true,
                true);
    }

    @Test
    public void ctor_withNameAndListAndExNonExistAndIsUnixLike() {
        new FolderFileWin(
                new NameFolderRoot(),
                new ArrayList<String>(){{
                    add("/home/text.txt");
                }},
                true,
                true);
    }

    @Test
    public void ctor_withNameAndRawExNonExist() {
        new FolderFileWin(
                new NameFolderRoot(),
                new ArrayList<String>(){{
                    add("/home/text.txt");
                }},
                true);
    }

    @Test
    public void shortName() {
        Folder<File> folder = new FolderFileWin("C:\\home\\text.txt");
        assertEquals(folder.shortName(), "/");
    }

    @Test
    public void fullName() {
        Folder<File> folder = new FolderFile("C:\\home\\text.txt");
        assertEquals(folder.fullName(), "/");
    }

    @Test
    public void files() throws NotFoundException {
        Separator separator = new SeparatorWin();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> root = new FolderFileWin(
                exDir.name() + "root1.txt",
                exDir.name() + "root2.txt",
                exDir.name() + "root3",
                exDir.name() + "music" + separator.pure() + "audio1.mp3",
                exDir.name() + "music" + separator.pure() + "audio2.mp3",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"
        );

        List<File> expected = new ArrayList<>();
        expected.add(new File(exDir.name() + "root1.txt"));
        expected.add(new File(exDir.name() + "root2.txt"));
        expected.add(new File(exDir.name() + "root3"));
        expected.add(new File(exDir.name() + "music" + separator.pure() + "audio1.mp3"));
        expected.add(new File(exDir.name() + "music" + separator.pure() + "audio2.mp3"));
        expected.add(new File(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac"));
        expected.add(new File(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"));

        root.files();

        AtomicInteger i = new AtomicInteger();
        Folder<File> actualF = new SearchByStartWith<>(new SearchFoldersByRegex<>(root))
                .answer("/\\" + "C:" + exDir.name().substring(0, exDir.name().length() - 1))
                .get(0);
        List<File> actual = actualF.files();

        if(isWindows) {
            actual.forEach(file -> assertEquals(file, expected.get(i.getAndIncrement())));
        }
    }

    @Test
    public void files_unixLike() throws NotFoundException {
        Separator separator = new SeparatorUnix();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> root = new FolderFileWin(
                false,
                true,
                exDir.name() + "root1.txt",
                exDir.name() + "root2.txt",
                exDir.name() + "root3",
                exDir.name() + "music" + separator.pure() + "audio1.mp3",
                exDir.name() + "music" + separator.pure() + "audio2.mp3",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"
        );

        Separator sepWin = new SeparatorWin();
        Directory<String> exDirWin = new ExampleDirectory(
                new CurrentDirectory(sepWin),
                sepWin);
        List<File> expected = new ArrayList<>();
        expected.add(new File(exDirWin.name() + "root1.txt"));
        expected.add(new File(exDirWin.name() + "root2.txt"));
        expected.add(new File(exDirWin.name() + "root3"));
        expected.add(new File(exDirWin.name() + "music" + sepWin.pure() + "audio1.mp3"));
        expected.add(new File(exDirWin.name() + "music" + sepWin.pure() + "audio2.mp3"));
        expected.add(new File(exDirWin.name() + "music" + sepWin.pure() + "opus" + sepWin.pure() + "02.flac"));
        expected.add(new File(exDirWin.name() + "music" + sepWin.pure() + "opus" + sepWin.pure() + "o1"));

        AtomicInteger i = new AtomicInteger();
        Folder<File> actualF = new SearchByStartWith<>(new SearchFoldersByRegex<>(root))
                .answer("/" + sepWin.pure() + "C:" + exDirWin.name().substring(0, exDirWin.name().length() - 1))
                .get(0);
        List<File> actual = actualF.files();

        if(isWindows) {
            actual.forEach(file -> assertEquals(file, expected.get(i.getAndIncrement())));
        }
    }

    @Test
    public void children() {
        Separator separator = new SeparatorWin();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> root = new FolderFileWin(
                exDir.name() + "root1.txt",
                exDir.name() + "root2.txt",
                exDir.name() + "root3",
                exDir.name() + "music" + separator.pure() + "audio1.mp3",
                exDir.name() + "music" + separator.pure() + "audio2.mp3",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1");

        Folder<File> opusF = new FolderPure<>(
                exDir.name() + "music" + separator.pure() + "opus",
                new File(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac"),
                new File(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"));

        Folder<File> musicF = new FolderPure<>(
                exDir.name() + "music",
                new ArrayList<File>() {{
                    add(new File(exDir.name() + "music" + separator.pure() + "audio1.mp3"));
                    add(new File(exDir.name() + "music" + separator.pure() + "audio2.mp3"));
                }},
                opusF);

        Folder<File> exF = new FolderPure<>(
                exDir.name(),
                new ArrayList<File>() {{
                    add(new File(exDir.name() + "root1.txt"));
                    add(new File(exDir.name() + "root2.txt"));
                    add(new File(exDir.name() + "root3"));
                }},
                musicF);

        List<Folder<File>> expected = new ArrayList<>();
        expected.add(exF);
        expected.add(musicF);
        expected.add(opusF);

        root.children();

        AtomicInteger i = new AtomicInteger();
        List<Folder<File>> actual = root.children();

        if(isWindows) {
            actual.get(0).travel(folder -> {
                if (folder.fullName().contains(exDir.name().substring(0, exDir.name().length() - 1))) {
                    assertEquals(folder, expected.get(i.getAndIncrement()));
                }
            });
        }
    }

    @Test
    public void files_withNotRootName() {
        Folder<File> actual = new FolderFileWin(
                new NamePure("C:\\music", new SeparatorWin()),
                "C:\\music\\misc\\1.mp3",
                "C:\\music\\misc\\2.mp3",
                "C:\\music\\misc\\3.mp3",
                "C:\\music\\ms.mp3"
        );

        Folder<File> expected = new FolderPure<>(
                new NamePure("/\\C:\\music", new SeparatorWin()),
                new ArrayList<File>() {{ add(new File("C:\\music\\ms.mp3")); }},
                new FolderPure<>(
                        new NamePure("/\\C:\\music\\misc", new SeparatorWin()),
                        new File("C:\\music\\misc\\1.mp3"),
                        new File("C:\\music\\misc\\2.mp3"),
                        new File("C:\\music\\misc\\3.mp3")
                )
        );

        assertEquals(expected, actual);
    }

    @Test
    public void files_withWrongRootName() {
        Folder<File> actual = new FolderFileWin(
                new NamePure("C:\\documents", new SeparatorWin()),
                "C:\\music\\misc\\1.mp3",
                "C:\\music\\misc\\2.mp3",
                "C:\\music\\misc\\3.mp3",
                "C:\\music\\ms.mp3"
        );

        Folder<File> expected = new FolderPure<>(
                new NameFolderRoot(),
                new ArrayList<>(),
                new FolderPure<>(
                        new NamePure("/\\C:", new SeparatorWin()),
                        new ArrayList<>(),
                        new FolderPure<>(
                                new NamePure("/\\C:\\music", new SeparatorWin()),
                                new ArrayList<File>() {{ add(new File("C:\\music\\ms.mp3")); }},
                                new FolderPure<>(
                                        new NamePure("/\\C:\\music\\misc", new SeparatorWin()),
                                        new File("C:\\music\\misc\\1.mp3"),
                                        new File("C:\\music\\misc\\2.mp3"),
                                        new File("C:\\music\\misc\\3.mp3")
                                )
                        )
                )
        );

        assertEquals(expected, actual);
    }

    @Test
    public void eq() {
        Folder<File> actual = new FolderFileWin(
                "C:\\file.txt");
        Folder<File> expected = new FolderPure<>(
                new NameFolderRoot(),
                new ArrayList<>(),
                new FolderPure<>(
                        new NamePure("/\\C:", new SeparatorWin()),
                        new File("C:\\file.txt"))
        );
        assertEquals(expected, actual);
    }

    @Test
    public void eq_wrongType() {
        Folder<File> actual = new FolderFileWin(
                "C:\\file.txt");
        String fakeFolder = "C:\\";
        assertNotEquals(fakeFolder, actual);
    }

    @Test
    public void eq_no() {
        Folder<File> actual = new FolderFileWin(
                "C:\\file.txt",
                "C:\\file2.txt");
        Folder<File> expected = new FolderPure<>("C:",
                new File("C:\\file.txt"));
        assertNotEquals(expected, actual);
    }
}
