package ru.viise.papka.filter;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FilterFilesUniqueNormalizeTestNG {

    private List<String> rawFiles;
    private List<String> expected;

    @BeforeClass
    public void beforeClass() {
        rawFiles = new ArrayList<>();
        rawFiles.add("/root1.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root2.txt");
        rawFiles.add("/root3.txt");
        rawFiles.add("/folder1/fl1.txt");
        rawFiles.add("/folder1/fl1.txt");
        rawFiles.add("/folder1/fl2.txt");

        expected = new ArrayList<>();
        expected.add("/root1.txt");
        expected.add("/root2.txt");
        expected.add("/root3.txt");
        expected.add("/folder1/fl1.txt");
        expected.add("/folder1/fl2.txt");
    }

    @Test
    public void apply() {
        Filter<List<String>> filter = new FilterFilesUniqueNormalize<>(rawFiles);
        List<String> actual = filter.apply();

        assertEquals(actual, expected);
    }
}
