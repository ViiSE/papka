package ru.viise.papka.find;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.entity.NameFolderRoot;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class FindByExtTestNG {

    private Find<List<String>, String> find;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");
        rootFiles.add("child1.txt");
        rootFiles.add("child1.png");
        rootFiles.add("child2.txt");
        rootFiles.add("child2.png");
        rootFiles.add("child2_1.txt");
        rootFiles.add("child2_1.png");

        Folder<String> root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles);

        find = new FindByExt<>(
                new FindFilesByRegex(root, false));
    }

    @Test
    public void answer() throws NotFoundException {
        List<String> actual = find.answer(".txt");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("child1.txt");
        expected.add("child2.txt");
        expected.add("child2_1.txt");

        assertEquals(expected, actual);
    }

    @Test
    public void answer_multipleExt() throws NotFoundException {
        List<String> actual = find.answer(".pdf|.png");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("root1.png");
        expected.add("root2.pdf");
        expected.add("child1.png");
        expected.add("child2.png");
        expected.add("child2_1.png");


        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        find.answer(".jpeg");
    }
}
