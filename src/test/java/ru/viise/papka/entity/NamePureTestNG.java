package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.system.SeparatorWin;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class NamePureTestNG {

    private final String fullName = "/music/opus/opus111.mp3";
    private final String shortName = "opus111.mp3";
    private Name name;

    @BeforeClass
    public void beforeClass() {
        name = new NamePure(fullName);
    }

    @Test
    public void shortName() {
        assertEquals(name.shortName(), shortName);
    }

    @Test
    public void shortNameBadSymbols() {
        String badName = "/music/op*?\"<>|+\u0000us/opus111.mp3";
        Name name = new NamePure(badName);
        assertEquals(name.shortName(), shortName);
        assertEquals(name.fullName(), fullName);
    }

    @Test
    public void fullName() {
        assertEquals(name.fullName(), fullName);
    }

    @Test
    public void fullName_win() {
        Name nameWin = new NamePure("C:\\folder\\file.txt", new SeparatorWin());
        assertEquals(nameWin.fullName(), "C:\\folder\\file.txt");
    }

    @Test
    public void fullName_winWithRoot() {
        Name nameWin = new NamePure("/\\C:\\folder\\file.txt", new SeparatorWin());
        assertEquals(nameWin.fullName(), "/\\C:\\folder\\file.txt");
    }

    @Test
    public void shortName_winWithRoot() {
        Name nameWin = new NamePure("/\\C:\\folder\\file.txt", new SeparatorWin());
        assertEquals(nameWin.shortName(), "file.txt");
    }

    @Test
    public void fullName_winUnixLike() {
        Name nameWin = new NamePure("C:/folder/file.txt", new SeparatorWin());
        assertEquals(nameWin.fullName(), "C:\\folder\\file.txt");
    }

    @Test
    public void shortName_win() {
        Name nameWin = new NamePure("C:\\folder\\file.txt", new SeparatorWin());
        assertEquals(nameWin.shortName(), "file.txt");
    }

    @Test
    public void shortName_oneSymbol() {
        Name nameWin = new NamePure("/");
        assertEquals(nameWin.shortName(), "/");
    }

    @Test
    public void fullName_oneSymbol() {
        Name nameWin = new NamePure("/");
        assertEquals(nameWin.fullName(), "/");
    }

    @Test
    public void equals() {
        Name eqName = new NamePure(fullName);
        assertEquals(eqName, name);
    }

    @Test
    public void equals_not() {
        Name notEqName = new NamePure("/folder/file.txt");
        assertNotEquals(notEqName, name);
    }

    @Test
    public void equals_wrongType() {
        Name name = new NamePure("/folder/file.txt");
        List<String> fake = new ArrayList<>();
        assertNotEquals(fake, name);
    }
}
