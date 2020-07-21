package ru.viise.papka.find;

import ru.viise.papka.exception.NotFoundException;

public class FindByExt<T> implements Find<T, String> {

    private final Find<T, String> find;

    public FindByExt(Find<T, String> find) {
        this.find = find;
    }

    @Override
    public T answer(String ext) throws NotFoundException {
        String extReg = ext.replaceAll("\\.", "\\\\.");
        return find.answer("([^.]*)(" + extReg + "$)");
    }
}
