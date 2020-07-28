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

import static org.testng.Assert.assertEquals;

public class PreparedFoldersMapFileTestNG {

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
    public void preparation() {
        Map<String, Folder<File>> mapFile = new PreparedFoldersMapFile(prepFolders).preparation();

        AtomicInteger i = new AtomicInteger();
        mapFile.forEach((fullFolderName, folder) -> {
            List<String> filesName = files.get(i.getAndIncrement());
            AtomicInteger j = new AtomicInteger();
            folder.files().forEach(file -> assertEquals(filesName.get(j.getAndIncrement()), file.getName()));
        });
    }

    @Test
    public void preparation_excludeNonExisting() {
        Separator separator = new SeparatorUnix();
        Directory<String> exDir = new ExampleDirectory(
                new CurrentDirectory(
                        separator),
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
        expected.add(new FolderPure<>(exDir.name(), root));
        expected.add(new FolderPure<>(exDir.name() + "music", music));
        expected.add(new FolderPure<>(exDir.name() + "music" + separator.pure() + "opus", opus));

        List<String> rawFiles = new ArrayList<>();
        rawFiles.add(exDir.name() + "root1.txt");
        rawFiles.add(exDir.name() + "root2");
        rawFiles.add(exDir.name() + "root3.txt");
        rawFiles.add(exDir.name() + "music" + separator.pure() + "audio1.mp3");
        rawFiles.add(exDir.name() + "music" + separator.pure() + "audio2.mp3");
        rawFiles.add(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac");
        rawFiles.add(exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1");
        rawFiles.sort(String::compareTo);

        Map<String, Folder<File>> actual = new PreparedFoldersMapFile(
                new PreparedFoldersMapRaw(rawFiles),true)
                .preparation();

        AtomicInteger i = new AtomicInteger();
        actual.forEach((fullFolderName, folder) -> {
            if(!(fullFolderName.equals("/")))
                assertEquals(folder, expected.get(i.getAndIncrement()));
        });
    }
}
