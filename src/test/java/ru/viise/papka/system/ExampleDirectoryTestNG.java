package ru.viise.papka.system;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class ExampleDirectoryTestNG {

    private Directory<String> dir;

    @BeforeClass
    public void beforeClass() {
        dir = new ExampleDirectory(
                new CurrentDirectory(new SeparatorUnix()),
                new SeparatorUnix());
    }

    @Test
    public void name() {
        String dirName = dir.name();
        System.out.println(dirName);
        assertTrue(dirName.contains("example"));
    }
}
