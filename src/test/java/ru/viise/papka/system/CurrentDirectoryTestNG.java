package ru.viise.papka.system;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CurrentDirectoryTestNG {

    private Directory<String> dir;

    @BeforeClass
    public void beforeClass() {
        dir = new CurrentDirectory(new SeparatorUnix());
    }

    @Test
    public void name() {
        String dirName = dir.name();
        System.out.println(dirName);
    }
}
