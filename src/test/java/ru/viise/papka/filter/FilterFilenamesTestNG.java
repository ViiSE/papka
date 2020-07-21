package ru.viise.papka.filter;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.entity.NameFolderRoot;
import ru.viise.papka.find.FindByStartWith;
import ru.viise.papka.find.FindDuplicatesByList;
import ru.viise.papka.find.FindFilesByFolderNameRegex;
import ru.viise.papka.find.FindUniqueByList;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FilterFilenamesTestNG {

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
    public void apply_withoutException() {
        Filter<List<String>> filter = new FilterFilenames<>(
                new FindByStartWith<>(
                        new FindFilesByFolderNameRegex<>(folder, false)),
                "child2");
        List<String> actual = filter.apply();

        List<String> expected = new ArrayList<>();
        expected.add("child2.txt");
        expected.add("child2.png");
        expected.add("child2_1.txt");
        expected.add("child2_1.png");

        assertEquals(actual, expected);
    }

    @Test
    public void apply_tryCallException() {
        Filter<List<String>> filter = new FilterFilenames<>(
                new FindByStartWith<>(
                        new FindFilesByFolderNameRegex<>(folder)),
                "music");

        List<String> actual = filter.apply();
        assertEquals(actual, new ArrayList<>());
    }
}
