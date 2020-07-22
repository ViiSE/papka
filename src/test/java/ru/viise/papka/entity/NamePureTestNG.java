package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
    public void equals() {
        Name eqName = new NamePure(fullName);
        assertEquals(name, eqName);
    }

    @Test
    public void equals_not() {
        Name notEqName = new NamePure("/folder/file.txt");
        assertNotEquals(name, notEqName);
    }
}
