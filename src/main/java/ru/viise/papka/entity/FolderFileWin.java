package ru.viise.papka.entity;

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;
import ru.viise.papka.system.SeparatorWin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FolderFileWin implements Folder<File> {

    private final Name name;
    private List<File> files;
    private List<Folder<File>> children;

    private final List<String> rawFiles;
    private final boolean excludeNonExisting;
    private final boolean isUnixLike;

    public FolderFileWin(String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)));
    }

    public FolderFileWin(Name name, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), false, false);
    }

    public FolderFileWin(boolean excludeNonExisting, String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, false);
    }

    public FolderFileWin(boolean excludeNonExisting, boolean isUnixLike, String... rawFiles) {
        this(new NameFolderRoot(), new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, isUnixLike);
    }

    public FolderFileWin(Name name, boolean excludeNonExisting, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, false);
    }

    public FolderFileWin(Name name, boolean excludeNonExisting, boolean isUnixLike, String... rawFiles) {
        this(name, new ArrayList<>(Arrays.asList(rawFiles)), excludeNonExisting, isUnixLike);
    }

    public FolderFileWin(List<String> rawFiles) {
        this(new NameFolderRoot(), rawFiles);
    }

    public FolderFileWin(List<String> rawFiles, boolean excludeNonExisting) {
        this(new NameFolderRoot(), rawFiles, excludeNonExisting, false);
    }

    public FolderFileWin(List<String> rawFiles, boolean excludeNonExisting, boolean isUnixLike) {
        this(new NameFolderRoot(), rawFiles, excludeNonExisting, isUnixLike);
    }

    public FolderFileWin(Name name, List<String> rawFiles) {
        this(name, rawFiles, false, false);
    }

    public FolderFileWin(Name name, List<String> rawFiles, boolean excludeNonExisting) {
        this(name, rawFiles, excludeNonExisting, false);
    }

    public FolderFileWin(Name name, List<String> rawFiles, boolean excludeNonExisting, boolean isUnixLike) {
        this.name = name;
        this.rawFiles = rawFiles;
        this.excludeNonExisting = excludeNonExisting;
        this.isUnixLike = isUnixLike;
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

        Separator rawSeparator = isUnixLike ? new SeparatorUnix() : new SeparatorWin();
        FoldersFile<String, String> fsWin = new FoldersFileNameWin(rawSeparator);

        TreeFolder<File> trFolder = new TreeFolderPure<>(
                new FoldersFileNameWin(new SeparatorWin()),
                new PreparedFoldersMapFileWin(
                        new PreparedFoldersMapRaw(
                                fsWin,
                                rawSeparator,
                                rawFiles
                        ),
                        excludeNonExisting,
                        isUnixLike
                ),
                new SeparatorWin());

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
