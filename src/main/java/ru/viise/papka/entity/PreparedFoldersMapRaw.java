package ru.viise.papka.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PreparedFoldersMapRaw implements PreparedFolders<Map<String, List<String>>> {

    private final FoldersFile<String, String> foldersFile;
    private final List<String> rawFiles;

    public PreparedFoldersMapRaw(List<String> rawFiles) {
        this(new FoldersFileName(), rawFiles);
    }

    public PreparedFoldersMapRaw(FoldersFile<String, String> foldersFile, List<String> rawFiles) {
        this.foldersFile = foldersFile;
        this.rawFiles = rawFiles;
    }

    @Override
    public Map<String, List<String>> preparation() {
        Map<String, List<String>> folderMap = new TreeMap<>();
        folderMap.put(new NameFolderRoot().fullName(), new ArrayList<>());

        for(String fullFilename: rawFiles) {
            String shortFilename = new NamePure(fullFilename).shortName();
            List<String> foldersName = foldersFile.folders(fullFilename);
            if(foldersName.size() == 1) // root
                folderMap.get("/").add(shortFilename);
            else {
                String fullFoldersPath = String.join("/", foldersName).replaceAll("//", "/") + "/";
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

        return folderMap;
    }
}
