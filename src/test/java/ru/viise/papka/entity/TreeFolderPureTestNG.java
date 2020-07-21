package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class TreeFolderPureTestNG {

    private Folder<String> folder;
    private TreeFolder<String> treeFolder;

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

        folder = new FolderPure<>(
                        new NameFolderRoot(),
                        rootFiles,
                new FolderPure<>(
                "/folder1",
                        folder1Files),
                new FolderPure<>(
                        "/folder2",
                        folder2Files));

        PreparedFolders<Map<String, Folder<String>>> prepFolders = new PreparedFoldersMapText(
                new PreparedFoldersMapRaw(rawFiles));

        treeFolder = new TreeFolderPure<>(
                new FoldersFileName(),
                prepFolders);
    }

    @Test
    public void build() {
        Folder<String> actual = treeFolder.grow();
        assertEquals(folder, actual);
    }
}
