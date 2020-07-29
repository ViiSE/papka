package ru.viise.papka.search;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderFile;
import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.system.*;

import java.io.File;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SearchFilesByShortNameRegexTestNG {

    private Folder<File> root;
    private Separator separator;
    private Directory<String> exDir;


    @BeforeClass
    public void beforeClass() {
        separator = new SeparatorUnix();
        exDir = new ExampleDirectory(
                new CurrentDirectory(separator),
                separator);

        root = new FolderFile(
                exDir.name() + "root1.txt",
                exDir.name() + "root2.txt",
                exDir.name() + "root3",
                exDir.name() + "music" + separator.pure() + "audio1.mp3",
                exDir.name() + "music" + separator.pure() + "audio2.mp3",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "02.flac",
                exDir.name() + "music" + separator.pure() + "opus" + separator.pure() + "o1"
        );
    }

    @Test
    public void answer() throws NotFoundException {
        Folder<File> root = new FolderFile("root1.txt");
        Search<List<File>, String> search = new SearchFilesByShortNameRegex(root);
        List<File> files = search.answer("root1.txt");
        File actual = files.get(0);
        File expected = new File("/root1.txt");

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        Folder<File> root = new FolderFile("root1.txt");
        Search<List<File>, String> search = new SearchFilesByShortNameRegex(root);
        search.answer("root3.txt");
    }

    @Test
    public void answer_includeChildren() throws NotFoundException {
        Search<List<File>, String> search = new SearchFilesByShortNameRegex(root, true);
        List<File> files = search.answer("audio1.mp3");
        File actual = files.get(0);
        File expected = new File(exDir.name() + "music" + separator.pure() + "audio1.mp3");
        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_includeChildrenNotFound() throws NotFoundException {
        Search<List<File>, String> search = new SearchFilesByShortNameRegex(root, true);
        search.answer("audio134.mp3");
    }
}
