package ru.viise.papka.filter;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.find.Find;
import ru.viise.papka.find.FindFolders;

import java.util.ArrayList;
import java.util.List;

public class FilterFolders<T> implements Filter<List<Folder<T>>> {

    private final Find<List<Folder<T>>, String> findFolders;
    private final String pattern;

    public FilterFolders(Find<List<Folder<T>>, String> findFolders, String pattern) {
        this.findFolders = findFolders;
        this.pattern = pattern;
    }

    @Override
    public List<Folder<T>> apply() {
        try {
            return findFolders.answer(pattern);
        } catch (NotFoundException ex) {
            return new ArrayList<>();
        }
    }
}
