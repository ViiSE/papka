package ru.viise.papka.find;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FindDuplicatesByListTestNG {

    private Find<List<String>, List<String>> find;

    @BeforeClass
    public void beforeClass() {
        find = new FindDuplicatesByList<>();
    }

    @Test
    public void answer_withDuplicates() throws NotFoundException {
        List<String> list = new ArrayList<String>() {{
            add("/music/opus/opus111.mp3");
            add("/music/opus/opus109.mp3");
            add("/music/opus/opus111.mp3");
        }};

        List<String> expected = new ArrayList<String>() {{
            add("/music/opus/opus111.mp3");
        }};
        List<String> actual = find.answer(list);

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_noDuplicates() throws NotFoundException {
        List<String> list = new ArrayList<String>() {{
            add("/music/opus/opus111.mp3");
            add("/music/opus/opus109.mp3");
        }};

         find.answer(list);
    }
}
