package ru.viise.papka.find;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.entity.NameFolderRoot;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FindFolderByFullNameTestNG {

    private FindFolder<String, String> find;
    private Folder<String> root;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");
        rootFiles.add("childRoot1.pdf");
        rootFiles.add("childRoot2.pdf");

        List<String> child1Files = new ArrayList<>();
        child1Files.add("child1.txt");
        child1Files.add("child1.png");

        List<String> child2Files = new ArrayList<>();
        child2Files.add("child2.txt");
        child2Files.add("child2.png");

        List<String> child21Files = new ArrayList<>();
        child21Files.add("child21.txt");
        child21Files.add("child21.png");

        root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new FolderPure<>(
                        "/child1",
                        child1Files),
                new FolderPure<>(
                        "/child2",
                        child2Files,
                        new FolderPure<>(
                                "/child2/child21",
                                child21Files)));
        find = new FindFolderByFullName<>(root);
    }

    @Test
    public void answer_found() throws NotFoundException {
        Folder<String> actual = find.answer("/child2/child21");

        Folder<String> expected = new FolderPure<>(
                "/child2/child21",
                new ArrayList<String>() {{
                        add("child21.txt");
                        add("child21.png");
                    }});

        assertEquals(expected, actual);
    }

    @Test
    public void answer_fastFound() throws NotFoundException {
        Folder<String> actual = find.answer("/");

        assertEquals(root, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        find.answer("/music");
    }
}
