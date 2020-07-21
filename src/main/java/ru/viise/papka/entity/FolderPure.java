package ru.viise.papka.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FolderPure<T> implements Folder<T> {

    private final Name name;
    private final List<T> files;
    private final List<Folder<T>> children;

    public FolderPure(String fullName, List<T> files) {
        this(fullName, files, new ArrayList<>());
    }

    @SafeVarargs
    public FolderPure(String fullName, T... files) {
        this(fullName, new ArrayList<>(Arrays.asList(files)), new ArrayList<>());
    }

    @SafeVarargs
    public FolderPure(String fullName, List<T> files, Folder<T>... children) {
        this(new NamePure(fullName), files, new ArrayList<>(Arrays.asList(children)));
    }

    public FolderPure(String fullName, List<T> files, List<Folder<T>> children) {
        this(new NamePure(fullName), files, children);
    }

    public FolderPure(Name name, List<T> files) {
        this(name, files, new ArrayList<>());
    }

    @SafeVarargs
    public FolderPure(Name name, T... files) {
        this(name, new ArrayList<>(Arrays.asList(files)), new ArrayList<>());
    }

    @SafeVarargs
    public FolderPure(Name name, List<T> files, Folder<T>... children) {
        this(name, files, new ArrayList<>(Arrays.asList(children)));
    }

    public FolderPure(Name name, List<T> files, List<Folder<T>> children) {
        this.name = name;
        this.files = files;
        this.children = children;
    }

    @Override
    public List<T> files() {
        return files;
    }

    @Override
    public void travel(Consumer<? super Folder<T>> folder) {
        folder.accept(this);
        for(Folder<T> child: children) {
            travel(child, folder);
        }
    }

    private void travel(Folder<T> child, Consumer<? super Folder<T>> flCons) {
        flCons.accept(child);
        for (Folder<T> _child : child.children()) {
            travel(_child, flCons);
        }
    }

    @Override
    public String shortName() {
        return name.shortName();
    }

    @Override
    public String fullName() {
        return name.fullName();
    }

    @Override
    public List<Folder<T>> children() {
        return children;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object folder) {
        if(!(folder instanceof Folder)) {
            return false;
        }

        if(((Folder) folder).fullName().equals(this.fullName())) {
            if(((Folder) folder).files().equals(this.files))
                if(((Folder) folder).children().equals(this.children))
                    return true;
        }

        return super.equals(folder);
    }
}
