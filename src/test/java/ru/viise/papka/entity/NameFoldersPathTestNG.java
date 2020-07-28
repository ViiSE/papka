package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.system.SeparatorWin;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class NameFoldersPathTestNG {

    private List<String> foldersName;

    @BeforeClass
    public void beforeClass() {
        foldersName = new ArrayList<>();
        foldersName.add("/");
        foldersName.add("C:");
        foldersName.add("files");
        foldersName.add("music");
        foldersName.add("opus");
    }

    @Test
    public void fullName_unix() {
        Name fPathName = new NameFoldersPath(foldersName);
        String fullName = fPathName.fullName();

        assertEquals(fullName, "/C:/files/music/opus/");
    }

    @Test
    public void shortName_unix() {
        Name fPathName = new NameFoldersPath(foldersName);
        String shortName = fPathName.shortName();

        assertEquals(shortName, "opus");
    }

    @Test
    public void fullName_win() {
        Name fPathName = new NameFoldersPath(foldersName, new SeparatorWin());
        String fullName = fPathName.fullName();

        assertEquals(fullName, "/\\C:\\files\\music\\opus\\");
    }

    @Test
    public void shortName_win() {
        Name fPathName = new NameFoldersPath(foldersName, new SeparatorWin());
        String shortName = fPathName.shortName();

        assertEquals(shortName, "opus");
    }
}
