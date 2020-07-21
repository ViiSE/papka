package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

public interface FindFolder<T, V> extends Find<Folder<T>, V> {
    Folder<T> answer(V query) throws NotFoundException;
}
