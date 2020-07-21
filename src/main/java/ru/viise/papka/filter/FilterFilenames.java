package ru.viise.papka.filter;

import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.find.Find;

import java.util.ArrayList;
import java.util.List;

public class FilterFilenames<T> implements Filter<List<T>> {

    private final Find<List<T>, String> find;
    private final String regex;

    public FilterFilenames(Find<List<T>, String> find, String regex) {
        this.find = find;
        this.regex = regex;
    }

    @Override
    public List<T> apply() {
        try {
            return find.answer(regex);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
