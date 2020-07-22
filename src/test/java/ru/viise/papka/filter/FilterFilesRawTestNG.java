package ru.viise.papka.filter;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.find.FindDuplicatesByList;
import ru.viise.papka.find.FindUniqueByList;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FilterFilesRawTestNG {

    private List<String> rawFiles;
    private List<String> expected;

    @BeforeClass
    public void beforeClass() {
        rawFiles = new ArrayList<>();
        rawFiles.add("/root1.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root3.txt");
        rawFiles.add("/folder1/fl1.txt");
        rawFiles.add("/folder1/fl1.txt");
        rawFiles.add("/folder1/fl2.txt");

        expected = new ArrayList<>();
        expected.add("/root1.txt");
        expected.add("/root3.txt");
        expected.add("/folder1/fl2.txt");
    }

    @Test
    public void apply_withoutException() {
        Filter<List<String>> filter = new FilterFilesRaw(
                new FindUniqueByList<>(),
                rawFiles);
        List<String> actual = filter.apply();

        assertEquals(actual, expected);
    }

    @Test
    public void apply_tryCallException() {
        Filter<List<String>> filter = new FilterFilesRaw(
                new FindDuplicatesByList<>(),
                expected);
        List<String> actual = filter.apply();

        assertEquals(actual, new ArrayList<>());
    }
}
