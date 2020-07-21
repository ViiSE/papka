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

public class FindFolderByShortNameTestNG {

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

        List<String> innerChild1Files = new ArrayList<>();
        innerChild1Files.add("innerChild1.txt");
        innerChild1Files.add("innerChild1.png");

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
                                child21Files),
                        new FolderPure<>(
                                "/child2/child1",
                                innerChild1Files)));
    }

    @Test
    public void answer_found() throws NotFoundException {
        FindFolder<String, String> find = new FindFolderByShortName<>(root);
        Folder<String> actual = find.answer("child1");

        Folder<String> expected = new FolderPure<>(
                "/child1",
                new ArrayList<String>() {{
                    add("child1.txt");
                    add("child1.png");
                }});

        assertEquals(expected, actual);
    }

    @Test
    public void answer_fastFind() throws NotFoundException {
        FindFolder<String, String> find = new FindFolderByShortName<>(root);
        Folder<String> actual = find.answer("/");

        assertEquals(root, actual);
    }

    @Test
    public void answer_foundLast() throws NotFoundException {
        FindFolder<String, String> find = new FindFolderByShortName<>(root, true);
        Folder<String> actual = find.answer("child1");

        Folder<String> expected = new FolderPure<>(
                "/child2/child1",
                new ArrayList<String>() {{
                    add("innerChild1.txt");
                    add("innerChild1.png");
                }});

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        FindFolder<String, String> find = new FindFolderByShortName<>(root, true);
        find.answer("/music");
    }
}
