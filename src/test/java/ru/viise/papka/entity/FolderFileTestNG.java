package ru.viise.papka.entity;

import org.testng.annotations.Test;
import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.search.SearchByStartWith;
import ru.viise.papka.search.SearchFoldersByRegex;
import ru.viise.papka.system.*;
import utils.PrintTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class FolderFileTestNG {

    @Test
    public void ctor_withNameAndVarargs() {
        new FolderFile(
                new NamePure("/"),
                "/home/hello.a");
    }

    @Test
    public void ctor_withExNonExistAndVarargs() {
        new FolderFile(
                true,
                "/home/hello.a");
    }

    @Test
    public void ctor_withNameAndExNonExistAndVarargs() {
        new FolderFile(
                new NamePure("/"),
                true,
                "/home/hello.a");
    }

    @Test
    public void ctor_withNameAndFoldersFileAndExNonExistAndVarargs() {
        new FolderFile(
                new NamePure("/"),
                new FoldersFileName(),
                true,
                "/home/hello.a");
    }

    @Test
    public void ctor_withList() {
        new FolderFile(
                new ArrayList<String>() {{
                    add("/home/hello.a");
                }});
    }

    @Test
    public void ctor_withListAndExNonExist() {
        new FolderFile(
                new ArrayList<String>() {{
                    add("/home/hello.a");
                }},
                true);
    }

    @Test
    public void ctor_withNameAndList() {
        new FolderFile(
                new NamePure("/"),
                new ArrayList<String>() {{
                    add("/home/hello.a");
                }});
    }

    @Test
    public void ctor_withNameAndListAndExNonExist() {
        new FolderFile(
                new NamePure("/"),
                new ArrayList<String>() {{
                    add("/home/hello.a");
                }},
                true);
    }

    @Test
    public void ctor_withNameAndListAndFoldersFileAndExNonExist() {
        new FolderFile(
                new NamePure("/"),
                new ArrayList<String>() {{
                    add("/home/hello.a");
                }},
                new FoldersFileName(),
                true);
    }

    @Test
    public void ctor_withNameAndTreeFolder() {
        new FolderFile(
                new NamePure("/"),
                new TreeFolderPure<>(
                        new FoldersFileName(),
                        new PreparedFoldersMapFile(
                                new PreparedFoldersMapRaw(
                                        new ArrayList<String>() {{
                                            add("/home/hello.a");
                                        }}
                                )
                        )
                ));
    }

    @Test
    public void shortName() {
        Folder<File> folder = new FolderFile("/home/folder/text.txt");
        assertEquals(folder.shortName(), "/");
    }

    @Test
    public void fullName() {
        Folder<File> folder = new FolderFile("/home/folder/text.txt");
        assertEquals(folder.fullName(), "/");
    }

    @Test
    public void files() throws NotFoundException {
        Separator separator = new SeparatorUnix();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> root = new FolderFile(
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
                .answer(exDir.name().substring(0, exDir.name().length() - 1))
                .get(0);
        List<File> actual = actualF.files();
        actual.forEach(file -> assertEquals(file, expected.get(i.getAndIncrement())));
    }

    @Test
    public void children() {
        Separator separator = new SeparatorUnix();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> root = new FolderFile(
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

        AtomicInteger i = new AtomicInteger();
        List<Folder<File>> actual = root.children();

        actual.get(0).travel(folder -> {
            if(folder.fullName().contains(exDir.name().substring(0, exDir.name().length() - 1))) {
                assertEquals(folder, expected.get(i.getAndIncrement()));
            }
        });
    }

    @Test
    public void eq() {
        Folder<File> actual = new FolderFile(
                "/home/root1.txt",
                "/home/root2.txt");

        Folder<File> expected = new FolderPure<>(
                new NameFolderRoot(),
                new ArrayList<>(),
                new FolderPure<>(
                        "/home",
                        new File("/home/root1.txt"),
                        new File("/home/root2.txt")
                ));

        assertEquals(expected, actual);
    }

    @Test
    public void travel() {
        Separator separator = new SeparatorUnix();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        Folder<File> root = new FolderFile(
                exDir.name() + "root1.txt",
                exDir.name() + "root2.txt",
                exDir.name() + "root3",
                exDir.name() + "music" + separator.pure() + "audio1.mp3",
                exDir.name() + "music" + separator.pure() + "audio2.mp3",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"
        );

        root.travel(PrintTest::printFolderFile);
    }

    @Test(priority = 13)
    public void files_withNotRootName() {
        Folder<File> actual = new FolderFile(
                new NamePure("/music"),
                "/music/misc/1.mp3",
                "/music/misc/2.mp3",
                "/music/misc/3.mp3",
                "/music/ms.mp3"
        );

        Folder<String> expected = new FolderPure<>(
                "/music",
                new ArrayList<String>() {{ add("/music/ms.mp3"); }},
                new FolderPure<>(
                        "/music/misc",
                        "/music/misc/1.mp3",
                        "/music/misc/2.mp3",
                        "/music/misc/3.mp3"
                )
        );

        assertNotEquals(expected, actual);
    }

    @Test
    public void eq_wrongType() {
        Folder<File> actual = new FolderFile(
                "/home/root1.txt",
                "/home/root2.txt");
        Object fakeFolder = "/home";

        assertNotEquals(fakeFolder, actual);
    }

    @Test
    public void eq_no() {
        Folder<File> actual = new FolderFile(
                "/home/root1.txt",
                "/home/root2.txt",
                "/home/root3.txt");

        Folder<File> expected = new FolderPure<>(
                "/home",
                new File("/home/root1.txt"),
                new File("/home/root2.txt"));

        assertNotEquals(expected, actual);
    }
}
