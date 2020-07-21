package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

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
    public void equals_wrongClass() {
        String fileFake = "file.png";
        assertNotEquals(root, fileFake);
    }
}
