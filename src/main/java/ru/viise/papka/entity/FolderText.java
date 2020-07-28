package ru.viise.papka.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FolderText implements Folder<String> {

    private final Name name;
    private List<String> files;
    private List<Folder<String>> children;
    private final TreeFolder<String> trFolder;

    public FolderText(List<String> rawFiles) {
        this(new NameFolderRoot(), rawFiles);
    }

    public FolderText(String... rawFiles) {
        this(new NameFolderRoot(), new TreeFolderPure<>(
                new PreparedFoldersMapText(
                        new PreparedFoldersMapRaw(new ArrayList<>(Arrays.asList(rawFiles))))
        ));
    }

    public FolderText(Name name, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)));
    }

    public FolderText(Name name, List<String> rawFiles) {
        this(name, new TreeFolderPure<>(
                new PreparedFoldersMapText(
                        new PreparedFoldersMapRaw(rawFiles))
        ));
    }

    public FolderText(Name name, TreeFolder<String> trFolder) {
        this.name = name;
        this.trFolder = trFolder;
    }

    @Override
    public List<String> files() {
        if(files == null)
            init();
        return files;
    }

    private void init() {
        files = new ArrayList<>();
        children = new ArrayList<>();
        Folder<String> prepRoot = trFolder.grow();
        this.files = prepRoot.files();
        this.children = prepRoot.children();
    }

    @Override
    public void travel(Consumer<? super Folder<String>> folder) {
        if(files == null)
            init();
        folder.accept(this);
        for(Folder<String> child: children) {
            travel(child, folder);
        }
    }

    private void travel(Folder<String> child, Consumer<? super Folder<String>> flCons) {
        flCons.accept(child);
        for (Folder<String> _child : child.children()) {
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
    public List<Folder<String>> children() {
        if(children == null)
            init();
        return children;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object folder) {
        if(!(folder instanceof Folder)) {
            return false;
        }

        if(((Folder) folder).fullName().equals(this.fullName())) {
            if(((Folder) folder).files().equals(files()))
                if(((Folder) folder).children().equals(children()))
                return true;
        }

        return super.equals(folder);
    }
}
