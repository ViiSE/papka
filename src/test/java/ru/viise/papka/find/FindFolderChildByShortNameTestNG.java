package ru.viise.papka.find;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.entity.Folder;
import ru.viise.papka.entity.FolderPure;
import ru.viise.papka.entity.NameFolderRoot;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FindFolderChildByShortNameTestNG {

    private Folder<String> root;

    @BeforeClass
    public void beforeClass() {
        List<String> rootFiles = new ArrayList<>();
        rootFiles.add("root1.png");
        rootFiles.add("root2.pdf");
        rootFiles.add("childRoot1.pdf");
        rootFiles.add("childRoot2.pdf");

        List<String> child1Files = new ArrayList<>();
        child1Files.add("child1.txt");
        child1Files.add("child1.png");

        List<String> child2Files = new ArrayList<>();
        child2Files.add("child2.txt");
        child2Files.add("child2.png");

        root = new FolderPure<>(
                new NameFolderRoot(),
                rootFiles,
                new FolderPure<>(
                        "/child1",
                        child1Files),
                new FolderPure<>(
                        "/child2",
                        child2Files));
    }

    @Test
    public void answer_found() throws NotFoundException {
        FindFolder<String, String> find = new FindFolderChildByShortName<>(root.children());
        Folder<String> actual = find.answer("child2");

        Folder<String> expected = new FolderPure<>(
                "/child2",
                new ArrayList<String>() {{
                        add("child2.txt");
                        add("child2.png");
                    }});

        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_notFound() throws NotFoundException {
        FindFolder<String, String> find = new FindFolderChildByShortName<>(root.children());
        find.answer("music");
    }
}
