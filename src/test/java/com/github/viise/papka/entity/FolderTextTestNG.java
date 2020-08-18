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

import org.testng.annotations.Test;
import utils.PrintTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

public class FolderTextTestNG {

    @Test(priority = 1)
    public void ctorArray() {
        new FolderText(
                "/root1.png",
                "/music/a.mp3",
                 "/music/opus/b.mp3",
                 "/music/opus/var/var.mp3",
                 "/music/opus/var/varsAr.mp3",
                 "/music/opus/bar/bar.mp3",
                 "/music/opus/dar/dar.flac",
                 "/opes/b.png",
                 "/music/opus/ar.mp3",
                 "/root2.png",
                 "/root3.png");
    }

    @Test(priority = 2)
    public void ctorList() {
        new FolderText(
                new ArrayList<String>() {{
                add("/root1.png");
                add("/music/a.mp3");
                add("/music/opus/b.mp3");
                add("/music/opus/var/var.mp3");
                add("/music/opus/var/varsAr.mp3");
                add("/music/opus/bar/bar.mp3");
                add("/music/opus/dar/dar.flac");
                add("/opes/b.png");
                add("/music/opus/ar.mp3");
                add("/root2.png");
                add("/root3.png"); }});
    }

    @Test(priority = 3)
    public void ctorNameAndArray() {
        new FolderText(
                new NameFolderRoot(),
                "/root1.png",
                "/music/a.mp3",
                "/music/opus/b.mp3",
                "/music/opus/var/var.mp3",
                "/music/opus/var/varsAr.mp3",
                "/music/opus/bar/bar.mp3",
                "/music/opus/dar/dar.flac",
                "/opes/b.png",
                "/music/opus/ar.mp3",
                "/root2.png",
                "/root3.png")
                .files();
    }

    @Test(priority = 4)
    public void ctorNameAndList() {
        new FolderText(
                new NameFolderRoot(),
                new ArrayList<String>() {{
                    add("/root1.png");
                    add("/music/a.mp3");
                    add("/music/opus/b.mp3");
                    add("/music/opus/var/var.mp3");
                    add("/music/opus/var/varsAr.mp3");
                    add("/music/opus/bar/bar.mp3");
                    add("/music/opus/dar/dar.flac");
                    add("/opes/b.png");
                    add("/music/opus/ar.mp3");
                    add("/root2.png");
                    add("/root3.png"); }});
    }

    @Test(priority = 5)
    public void children() {
        Folder<String> folders = testFolder();

        List<Folder<String>> children = folders.children();
        List<Folder<String>> expected = new ArrayList<>();
        assertNotNull(children);
        assertEquals(children, expected);
    }

    @Test(priority = 6)
    public void files() {
        Folder<String> folders = new FolderText(
                "/root124",
                "/root1.png",
                "/root2.png",
                "/root3.png");

        List<String> files = folders.files();
        assertNotNull(files);
        assertEquals(
                files,
                new ArrayList<String>() {{
                    add("root1.png");
                    add("root124");
                    add("root2.png");
                    add("root3.png");}});
    }

    @Test(priority = 7)
    public void fullName() {
        String fullName = testFolder().fullName();

        assertNotNull(fullName);
        assertEquals(fullName, "/");
    }

    @Test(priority = 8)
    public void travel() {
        List<String> rootFiles = new ArrayList<String>() {{
            add("root1.png");
            add("root2.png");
            add("root3.png");
        }};
        rootFiles.sort(String::compareTo);

        List<String> musicFiles = new ArrayList<String>() {{
            add("a.mp3");
        }};

        List<String> musicOpusFiles = new ArrayList<String>() {{
            add("b.mp3");
            add("ar.mp3");
        }};
        musicOpusFiles.sort(String::compareTo);

        List<String> musicOpusVarFiles = new ArrayList<String>() {{
            add("var.mp3");
        }};

        List<String> musicOpusBarFiles = new ArrayList<String>() {{
            add("bar.mp3");
        }};

        List<String> musicOpesFiles = new ArrayList<String>() {{
            add("b.png");
        }};

        List<List<String>> files = new ArrayList<>();
        files.add(rootFiles);
        files.add(musicFiles);
        files.add(musicOpusFiles);
        files.add(musicOpusBarFiles);
        files.add(musicOpusVarFiles);
        files.add(musicOpesFiles);

        AtomicInteger i = new AtomicInteger();
        Folder<String> root = new FolderText(
                "/root1.png",
                "/music/a.mp3",
                "/music/opus/b.mp3",
                "/music/opus/var/var.mp3",
                "/music/opus/bar/bar.mp3",
                "/opes/b.png",
                "/music/opus/ar.mp3",
                "/root2.png",
                "/root3.png");

        root.travel(folder -> {
            PrintTest.printFolder(folder);
            List<String> _files = folder.files();
            assertEquals(_files, files.get(i.getAndIncrement()));
        });
    }

    @Test(priority = 9)
    public void travel_twiceThenCheckInit() {
        Folder<String> root = new FolderText(
                "/root1.png",
                "/music/a.mp3",
                "/music/opus/b.mp3",
                "/music/opus/var/var.mp3",
                "/music/opus/bar/bar.mp3",
                "/opes/b.png",
                "/music/opus/ar.mp3",
                "/root2.png",
                "/root3.png");

        root.travel(PrintTest::printFolder);
        root.travel(PrintTest::printFolder);
    }

    @Test(priority = 10)
    public void shortName() {
        String shortName = testFolder().shortName();
        assertNotNull(shortName);
        assertEquals(shortName, "/");
    }

    @Test(priority = 11)
    public void eq() {
        Folder<String> actual = testFolder();
        Folder<String> expected = new FolderText(
                "/root124",
                "/root1.png",
                "/root2.png",
                "/root3.png");

        assertEquals(expected, actual);
    }

    @Test(priority = 12)
    public void eq_notFullName() {
        Folder<String> actual = testFolder();
        Folder<String> expected = new FolderText(
                "/music/root124",
                "/music/root1.png",
                "/music/root2.png",
                "/music/root3.png",
                "/music/root33.png");

        assertNotEquals(expected, actual);
    }

    @Test(priority = 13)
    public void eq_fullNameButFilesNot() {
        Folder<String> actual = testFolder();
        Folder<String> expected = new FolderText(
                "/root124",
                "/root1.png",
                "/root2.png",
                "/root3.png",
                "/root33.png");

        assertNotEquals(expected, actual);
    }

    @Test(priority = 14)
    public void eq_fullNameAndFilesButChildrenNot() {
        Folder<String> actual = testFolder();
        Folder<String> expected = new FolderText(
                "/root124",
                "/root1.png",
                "/root2.png",
                "/root3.png",
                "/music/root33.png");

        assertNotEquals(expected, actual);
    }

    @Test(priority = 15)
    public void eq_wrongType() {
        Folder<String> actual = testFolder();
        String folderFake = "/music";

        assertNotEquals(folderFake, actual);
    }

    @Test(priority = 16)
    public void eq_no() {
        Folder<String> actual = testFolder();
        Folder<String> expected = new FolderText(
                "/root1.png",
                "/root2.png",
                "/root3.png");
        assertNotEquals(expected, actual);
    }

    @Test(priority = 17)
    public void files_withNotRootName() {
        Folder<String> actual = new FolderText(
                new NamePure("/music"),
                "/music/misc/1.mp3",
                "/music/misc/2.mp3",
                "/music/misc/3.mp3",
                "/music/ms.mp3"
        );

        Folder<String> expected = new FolderPure<>(
                "/music",
                new ArrayList<String>() {{ add("ms.mp3"); }},
                new FolderPure<>(
                        "/music/misc",
                        "1.mp3",
                        "2.mp3",
                        "3.mp3"
                )
        );

        assertEquals(expected, actual);
    }

    @Test(priority = 18)
    public void files_withWrongRootName() {
        Folder<String> actual = new FolderText(
                new NamePure("/documents"),
                "/music/misc/1.mp3",
                "/music/misc/2.mp3",
                "/music/misc/3.mp3",
                "/music/ms.mp3"
        );

        Folder<String> expected = new FolderPure<>(
                "/",
                new ArrayList<>(),
                new FolderPure<>(
                        "/music",
                        new ArrayList<String>() {{ add("ms.mp3"); }},
                        new FolderPure<>(
                                "/music/misc",
                                "1.mp3",
                                "2.mp3",
                                "3.mp3"
                        )
                )
        );

        assertEquals(expected, actual);
    }

    private Folder<String> testFolder() {
        return new FolderText(
                "/root124",
                "/root1.png",
                "/root2.png",
                "/root3.png");

    }
}
