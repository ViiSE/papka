package ru.viise.papka.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FolderFile implements Folder<File> {

    private final Name name;
    private List<File> files;
    private List<Folder<File>> children;
    private final TreeFolder<File> trFolder;

    public FolderFile(String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)));
    }

    public FolderFile(Name name, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), false);
    }

    public FolderFile(boolean excludeNonExisting, String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting);
    }

    public FolderFile(Name name, boolean excludeNonExisting, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), new FoldersFileName(), excludeNonExisting);
    }

    public FolderFile(Name name, FoldersFile<String, String> foldersFile, boolean excludeNonExisting, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), foldersFile, excludeNonExisting);
    }

    public FolderFile(List<String> rawFiles) {
        this(new NameFolderRoot(), rawFiles);
    }

    public FolderFile(List<String> rawFiles, boolean excludeNonExisting) {
        this(new NameFolderRoot(), rawFiles, excludeNonExisting);
    }

    public FolderFile(Name name, List<String> rawFiles) {
        this(name, rawFiles, false);
    }

    public FolderFile(Name name, List<String> rawFiles, boolean excludeNonExisting) {
        this(name, rawFiles, new FoldersFileName(), excludeNonExisting);
    }

    public FolderFile(Name name, List<String> rawFiles, FoldersFile<String, String> foldersFile, boolean excludeNonExisting) {
        this(name, new TreeFolderPure<>(
                foldersFile,
                new PreparedFoldersMapFile(
                        new PreparedFoldersMapRaw(
                                foldersFile,
                                rawFiles),
                        excludeNonExisting)
        ));
    }

    public FolderFile(Name name, TreeFolder<File> trFolder) {
        this.name = name;
        this.trFolder = trFolder;
    }

    @Override
    public List<File> files() {
        if(files == null)
            init();
        return files;
    }

    private void init() {
        files = new ArrayList<>();
        children = new ArrayList<>();
        Folder<File> prepRoot = trFolder.grow();
        this.files = prepRoot.files();
        this.children = prepRoot.children();
    }

    @Override
    public void travel(Consumer<? super Folder<File>> folder) {
        if(files == null)
            init();
        folder.accept(this);
        for(Folder<File> child: children) {
            travel(child, folder);
        }
    }

    private void travel(Folder<File> child, Consumer<? super Folder<File>> flCons) {
        flCons.accept(child);
        for (Folder<File> _child : child.children()) {
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
    public List<Folder<File>> children() {
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
