package ru.viise.papka.entity;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PreparedFoldersMapText implements PreparedFolders<Map<String, Folder<String>>> {

    private final PreparedFolders<Map<String, List<String>>> prepFolders;

    public PreparedFoldersMapText(PreparedFolders<Map<String, List<String>>> prepFolders) {
        this.prepFolders = prepFolders;
    }

    @Override
    public Map<String, Folder<String>> preparation() {
        Map<String, List<String>> prepMap = prepFolders.preparation();

        Map<String, Folder<String>> mapText = new TreeMap<>();
        prepMap.forEach((fullFolderName, files) -> {
            Name name;
            if(fullFolderName.equals("/"))
                name = new NameFolderRoot();
            else
                name = new NamePure(fullFolderName);
            mapText.put(fullFolderName, new FolderPure<>(name, files));
        });
        return mapText;
    }
}
