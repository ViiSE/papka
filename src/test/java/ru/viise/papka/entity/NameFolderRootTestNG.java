package ru.viise.papka.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
}
