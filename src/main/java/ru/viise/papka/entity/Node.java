package ru.viise.papka.entity;

import java.util.List;

public interface Node<T> {
    List<T> children();
}
