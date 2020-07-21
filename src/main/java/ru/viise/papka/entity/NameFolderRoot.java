package ru.viise.papka.entity;

public class NameFolderRoot implements Name {

    @Override
    public String shortName() {
        return "/";
    }

    @Override
    public String fullName() {
        return "/";
    }
}
