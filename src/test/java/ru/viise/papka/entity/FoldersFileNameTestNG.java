package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FoldersFileNameTestNG {

    private FoldersFile<String, String> foldersFile;

    @BeforeClass
    public void beforeClass() {
        foldersFile = new FoldersFileName();
    }

    @Test
    public void folders_withRoot() {
        String rawFolder = "/music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_withoutRoot() {
        String rawFolder = "music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_doubleSlash() {
        String rawFolder = "/music//classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_withoutExt() {
        String rawFolder = "/music/classic/chopin";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_withoutFile() {
        String rawFolder = "/music/classic/chopin/";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("music");
            add("classic");
            add("chopin");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }
}
