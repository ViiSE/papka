package ru.viise.papka.entity;

import org.testng.annotations.Test;
import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;
import ru.viise.papka.system.SeparatorWin;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FoldersFileNameWinTestNG {

    @Test
    public void folders_unixWithoutRoot() {
        Separator separator = new SeparatorUnix();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "C:/music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_unixWithRoot() {
        Separator separator = new SeparatorUnix();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "/C:/music/classic/chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_winWithoutRoot() {
        Separator separator = new SeparatorWin();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "C:\\music\\classic\\chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_winWithRoot() {
        Separator separator = new SeparatorWin();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "/\\C:\\music\\classic\\chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }

    @Test
    public void folders_winWithoutSystemDrive() {
        Separator separator = new SeparatorWin();
        FoldersFile<String, String> foldersFile = new FoldersFileNameWin(separator);
        String rawFolder = "music\\classic\\chopin.mp3";
        List<String> expected = new ArrayList<String>() {{
            add("/");
            add("C:");
            add("music");
            add("classic");
        }};

        List<String> actual = foldersFile.folders(rawFolder);

        assertEquals(expected, actual);
    }
}
