package ru.viise.papka.entity;

import java.util.List;

public interface FoldersFile<T, V> {
    List<T> folders(V file);
}
