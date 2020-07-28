package ru.viise.papka.entity;

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;

import java.util.*;

public class PreparedFoldersMapRaw implements PreparedFolders<Map<String, List<String>>> {

    private final FoldersFile<String, String> foldersFile;
    private final Separator separator;
    private final List<String> rawFiles;

    public PreparedFoldersMapRaw(List<String> rawFiles) {
        this(new FoldersFileName(), rawFiles);
    }

    public PreparedFoldersMapRaw(FoldersFile<String, String> foldersFile, List<String> rawFiles) {
        this(foldersFile, new SeparatorUnix(), rawFiles);
    }

    public PreparedFoldersMapRaw(FoldersFile<String, String> foldersFile, Separator separator, List<String> rawFiles) {
        this.foldersFile = foldersFile;
        this.separator = separator;
        this.rawFiles = rawFiles;
    }

    @Override
    public Map<String, List<String>> preparation() {
        Map<String, List<String>> folderMap = new TreeMap<>();
        folderMap.put("/", new ArrayList<>());

        for(String fullFilename: rawFiles) {
            String shortFilename = new NamePure(fullFilename, separator).shortName();
            List<String> foldersName = foldersFile.folders(fullFilename);
            if(foldersName.size() == 1) // root
                folderMap.get("/").add(shortFilename);
            else {
                String fullFoldersPath = new NameFoldersPath(foldersName, separator).fullName();
                List<String> files = folderMap.get(fullFoldersPath);
                if(files != null) {
                    files.add(shortFilename);
                } else {
                    files = new ArrayList<>();
                    files.add(shortFilename);
                    folderMap.put(fullFoldersPath, files);
                }
            }
        }
        folderMap.values().forEach(Collections::sort);
        rawFiles.clear();

        return folderMap;
    }
}
