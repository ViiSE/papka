package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.List;

public interface FindFolders<T, V> extends Find<List<Folder<T>>, V> {
    List<Folder<T>> answer(V query) throws NotFoundException;
}
