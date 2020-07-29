package ru.viise.papka.search;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.entity.NameFolderRoot;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class SearchFilesByFolderNameRegexTestNG {

    private Folder<String> folder;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");

        List<String> child1 = new ArrayList<>();
        child1.add("child1.txt");
        child1.add("child1.png");

        List<String> child2 = new ArrayList<>();
        child2.add("child2.txt");
        child2.add("child2.png");

        List<String> child2_1 = new ArrayList<>();
        child2_1.add("child2_1.txt");
        child2_1.add("child2_1.png");

        folder = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new FolderPure<>(
                        "/child1",
                        child1),
                new FolderPure<>(
                        "/child2",
                        child2,
                        new FolderPure<>(
                                "/child1/child2_1",
                                child2_1))
        );
    }

    @Test
    public void ctor_isFullNameAndFolder() {
        new SearchFilesByFolderNameRegex<>(
                new FolderPure<>("folder"),
                true);
    }

    @Test
    public void ctor_folder() {
        new SearchFilesByFolderNameRegex<>(
                new FolderPure<>("folder"));
    }

    @Test
    public void answer_isFullName() throws NotFoundException {
        Search<List<String>, String> search = new SearchFilesByFolderNameRegex<>(folder);
        List<String> actual = search.answer("^/child2.*$");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("child2.txt");
        expected.add("child2.png");

        assertEquals(expected, actual);
    }

    @Test
    public void answer_isShortName() throws NotFoundException {
        Search<List<String>, String> search = new SearchFilesByFolderNameRegex<>(
                folder,
                false);
        List<String> actual = search.answer("^child2.*$");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        List<String> expected = new ArrayList<>();
        expected.add("child2.txt");
        expected.add("child2.png");
        expected.add("child2_1.txt");
        expected.add("child2_1.png");

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        Search<List<String>, String> search = new SearchFilesByFolderNameRegex<>(
                folder,
                false);
        search.answer("^chopin.*$");
    }
}