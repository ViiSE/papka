package ru.viise.papka.entity;

import java.util.List;
import java.util.function.Consumer;

public interface Folder<T> extends Node<Folder<T>>, Name {
    List<T> files();
    void travel(Consumer<? super Folder<T>> folder);
}
