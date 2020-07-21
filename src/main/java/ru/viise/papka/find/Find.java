package ru.viise.papka.find;

import ru.viise.papka.exception.NotFoundException;

public interface Find<T, V> {
    T answer(V query) throws NotFoundException;
}
