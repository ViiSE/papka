package ru.viise.papka.search;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SearchDuplicatesByListTestNG {

    private Search<List<String>, List<String>> search;

    @BeforeClass
    public void beforeClass() {
        search = new SearchDuplicatesByList<>();
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
        List<String> actual = search.answer(list);

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void answer_noDuplicates() throws NotFoundException {
        List<String> list = new ArrayList<String>() {{
            add("/music/opus/opus111.mp3");
            add("/music/opus/opus109.mp3");
        }};

         search.answer(list);
    }
}