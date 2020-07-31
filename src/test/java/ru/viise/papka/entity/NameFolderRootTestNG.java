package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class NameFolderRootTestNG {

    private final String rootName = "/";
    private Name name;

    @BeforeClass
    public void beforeClass() {
        name = new NameFolderRoot();
    }

    @Test
    public void shortName() {
        assertEquals(name.shortName(), rootName);
    }

    @Test
    public void fullName() {
        assertEquals(name.fullName(), rootName);
    }

    @Test
    public void eq() {
        Name expected = new NamePure("/");
        assertEquals(expected, name);
    }

    @Test
    public void eq_wrongType() {
        String expected = "/";
        assertNotEquals(expected, name);
    }

    @Test
    public void eq_no() {
        Name expected = new NamePure("/music");
        assertNotEquals(expected, name);
    }
}
