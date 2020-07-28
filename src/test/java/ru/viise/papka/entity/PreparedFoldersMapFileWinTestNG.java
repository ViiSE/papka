package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.system.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class PreparedFoldersMapFileWinTestNG {

    private List<String> rawFiles;
    private boolean isWindows;

    @BeforeClass
    public void beforeClass() {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

        Separator separator = new SeparatorWin();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);
        rawFiles = new ArrayList<>();
        rawFiles.add("C:" + exDir.name() + "root1.txt");
        rawFiles.add("C:" + exDir.name() + "root2");
        rawFiles.add("C:" + exDir.name() + "root3.txt");
        rawFiles.add("C:" + exDir.name() + "music" + separator.pure() + "audio1.mp3");
        rawFiles.add("C:" + exDir.name() + "music" + separator.pure() + "audio2.mp3");
        rawFiles.add("C:" + exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac");
        rawFiles.add("C:" + exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1");
        rawFiles.sort(String::compareTo);
    }

    @Test
    public void preparation() {
        rawFiles = rawFiles.stream()
                .map(s -> s.replace("/", "\\"))
                .collect(Collectors.toList());

        Separator separator = new SeparatorWin();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);
        rawFiles.add("/\\C:" + exDir.name() + "added.txt");
        rawFiles.sort(String::compareTo);

        List<File> root = new ArrayList<>();
        root.add(new File("C:" + exDir.name() + "root1.txt"));
        root.add(new File("C:" + exDir.name() + "root2"));
        root.add(new File("C:" + exDir.name() + "root3.txt"));
        root.add(new File("C:" + exDir.name() + "added.txt"));
        root.sort(Comparator.comparing(File::getName));

        List<File> music = new ArrayList<>();
        music.add(new File("C:" + exDir.name() + "music" + separator.pure() + "audio1.mp3"));
        music.add(new File("C:" + exDir.name() + "music" + separator.pure() + "audio2.mp3"));
        music.sort(Comparator.comparing(File::getName));

        List<File> opus = new ArrayList<>();
        opus.add(new File("C:" + exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac"));
        opus.add(new File("C:" + exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"));
        opus.sort(Comparator.comparing(File::getName));

        List<Folder<File>> expected = new ArrayList<>();
        expected.add(getFolder("/" + separator.pure() + "C:" + exDir.name(), root));
        expected.add(getFolder("/" + separator.pure() + "C:" + exDir.name() + "music", music));
        expected.add(getFolder("/" + separator.pure() + "C:" + exDir.name() + "music" + separator.pure() + "opus", opus));

        Map<String, Folder<File>> actual = new PreparedFoldersMapFileWin(
                new PreparedFoldersMapRaw(new FoldersFileNameWin(separator), separator, rawFiles))
                .preparation();

        AtomicInteger i = new AtomicInteger();
        actual.forEach((fullFolderName, folder) -> {
            if(!(fullFolderName.equals("/")))
                assertEquals(folder, expected.get(i.getAndIncrement()));
        });

        rawFiles.remove("/\\C:" + exDir.name() + "added.txt");
    }

    @Test
    public void preparation_excludeNonExisting() {
        rawFiles = rawFiles.stream()
                .map(s -> s.replace("/", "\\"))
                .collect(Collectors.toList());

        Separator separator = new SeparatorWin();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);
        List<File> root = new ArrayList<>();
        root.add(new File(exDir.name() + "root1.txt"));
        root.add(new File(exDir.name() + "root2"));
        root.sort(Comparator.comparing(File::getName));

        List<File> music = new ArrayList<>();
        music.add(new File(exDir.name() + "music" + separator.pure() + "audio1.mp3"));
        music.add(new File(exDir.name() + "music" + separator.pure() + "audio2.mp3"));
        music.sort(Comparator.comparing(File::getName));

        List<File> opus = new ArrayList<>();
        opus.add(new File(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac"));
        opus.add(new File(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"));
        opus.sort(Comparator.comparing(File::getName));

        List<Folder<File>> expected = new ArrayList<>();
        expected.add(getFolder("/" + separator.pure() + exDir.name(), root));
        expected.add(getFolder("/" + separator.pure() + exDir.name() + "music", music));
        expected.add(getFolder("/" + separator.pure() + exDir.name() + "music" + separator.pure() + "opus", opus));

        Map<String, Folder<File>> actual = new PreparedFoldersMapFileWin(
                new PreparedFoldersMapRaw(new FoldersFileNameWin(separator), separator, rawFiles),true)
                .preparation();

        if(isWindows) {
            AtomicInteger i = new AtomicInteger();
            actual.forEach((fullFolderName, folder) -> {
                if (!(fullFolderName.equals("/")))
                    assertEquals(folder, expected.get(i.getAndIncrement()));
            });
        }
    }

    @Test
    public void preparation_isUnixLike() {
        rawFiles = rawFiles.stream()
                .map(s -> s.replace("\\", "/"))
                .collect(Collectors.toList());

        Separator separator = new SeparatorWin();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);
        List<File> root = new ArrayList<>();
        root.add(new File("C:" + exDir.name() + "root1.txt"));
        root.add(new File("C:" + exDir.name() + "root2"));
        root.add(new File("C:" + exDir.name() + "root3.txt"));
        root.sort(Comparator.comparing(File::getName));

        List<File> music = new ArrayList<>();
        music.add(new File("C:" + exDir.name() + "music" + separator.pure() + "audio1.mp3"));
        music.add(new File("C:" + exDir.name() + "music" + separator.pure() + "audio2.mp3"));
        music.sort(Comparator.comparing(File::getName));

        List<File> opus = new ArrayList<>();
        opus.add(new File("C:" + exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac"));
        opus.add(new File("C:" + exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"));
        opus.sort(Comparator.comparing(File::getName));

        List<Folder<File>> expected = new ArrayList<>();
        expected.add(new FolderPure<>("/\\C:" + exDir.name(), root));
        expected.add(new FolderPure<>("/\\C:" + exDir.name() + "music", music));
        expected.add(new FolderPure<>("/\\C:" + exDir.name() + "music" + separator.pure() + "opus", opus));

        Map<String, Folder<File>> actual = new PreparedFoldersMapFileWin(
                new PreparedFoldersMapRaw(rawFiles),
                false,
                true)
                .preparation();

        AtomicInteger i = new AtomicInteger();
        actual.forEach((fullFolderName, folder) -> {
            if(!(fullFolderName.equals("/")))
                assertEquals(folder, expected.get(i.getAndIncrement()));
        });
    }

    private Folder<File> getFolder(String name, List<File> files) {
        return new FolderPure<>(
                new NamePure(name, new SeparatorWin()),
                files);
    }
}
