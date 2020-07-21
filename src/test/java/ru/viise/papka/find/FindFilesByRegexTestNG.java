package ru.viise.papka.find;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.entity.NameFolderRoot;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

public class FindFilesByRegexTestNG {

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
                        child2Files),
                new FolderPure<>(
                        "/child21",
                        child21Files));
    }

    @Test
    public void answer_excludeChildren() throws NotFoundException {
        Find<List<String>, String> find = new FindFilesByRegex(root);

        List<String> actual = find.answer("^child.*$");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("childRoot1.pdf");
        expected.add("childRoot2.pdf");

        assertEquals(expected, actual);
    }

    @Test
    public void answer_includeChildren() throws NotFoundException {
        Find<List<String>, String> find = new FindFilesByRegex(root, true);

        List<String> actual = find.answer("^child.*$");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("childRoot1.pdf");
        expected.add("childRoot2.pdf");
        expected.add("child1.txt");
        expected.add("child1.png");
        expected.add("child2.txt");
        expected.add("child2.png");
        expected.add("child21.txt");
        expected.add("child21.png");

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        Find<List<String>, String> find = new FindFilesByRegex(root, true);
        find.answer("opus");
    }
}
