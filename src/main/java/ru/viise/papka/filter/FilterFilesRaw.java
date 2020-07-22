package ru.viise.papka.filter;

import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.find.Find;

import java.util.ArrayList;
import java.util.List;

public class FilterFilesRaw implements Filter<List<String>> {

    private final Find<List<String>, List<String>> find;
    private final List<String> rawFiles;

    public FilterFilesRaw(Find<List<String>, List<String>> find, List<String> rawFiles) {
        this.find = find;
        this.rawFiles = rawFiles;
    }

    @Override
    public List<String> apply() {
        try {
            return find.answer(rawFiles);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
