package ru.viise.papka.find;

import ru.viise.papka.exception.NotFoundException;

import java.util.List;

public class FindByStartWith<T> implements Find<T, String> {

    private final Find<T, String> find;

    public FindByStartWith(Find<T, String> find) {
        this.find = find;
    }

    @Override
    public T answer(String startWith) throws NotFoundException {
        return find.answer("^" + startWith + ".*$");
    }
}
