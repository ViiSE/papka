package ru.viise.papka.entity;

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorWin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PreparedFoldersMapFileWin implements PreparedFolders<Map<String, Folder<File>>> {

    private final PreparedFolders<Map<String, List<String>>> prepFolders;
    private final boolean excludeNonExisting;
    private final boolean isUnixLike;

    public PreparedFoldersMapFileWin(PreparedFolders<Map<String, List<String>>> prepFolders) {
        this(prepFolders, false, false);
    }

    public PreparedFoldersMapFileWin(
            PreparedFolders<Map<String, List<String>>> prepFolders,
            boolean excludeNonExisting) {
        this(prepFolders, excludeNonExisting, false);
    }

    public PreparedFoldersMapFileWin(
            PreparedFolders<Map<String, List<String>>> prepFolders,
            boolean excludeNonExisting,
            boolean isUnixLike) {
        this.prepFolders = prepFolders;
        this.excludeNonExisting = excludeNonExisting;
        this.isUnixLike = isUnixLike;
    }

    @Override
    public Map<String, Folder<File>> preparation() {
        Separator separator = new SeparatorWin();
        Map<String, List<String>> prepMap = prepFolders.preparation();

        Map<String, Folder<File>> mapFile = new TreeMap<>();
        prepMap.forEach((fullFolderName, files) -> {
            Name name = new NameFolderRoot();
            if(!(fullFolderName.equals(name.fullName()))) {
                if(isUnixLike)
                    fullFolderName = fullFolderName.charAt(0) + separator.pure() + fullFolderName.substring(1);
                name = new NamePure(fullFolderName, separator);
            }

            if(folderIsExist(name)) {
                List<File> filesSystem = new ArrayList<>();
                for (String filename : files) {
                    String fullFilename = name.fullName() + separator.pure() + filename;
                    fullFilename = fullFilename.substring(2); // remove /\ in the beginning
                    File file = new File(fullFilename);
                    if (excludeNonExisting) {
                        if (file.exists())
                            filesSystem.add(file);
                    } else
                        filesSystem.add(file);
                }
                mapFile.put(fullFolderName, new FolderPure<>(name, filesSystem));
            }
        });

        if(isUnixLike) {
            Map<String, Folder<File>> normalizeMapFile = new TreeMap<>();
            mapFile.forEach((fullFolderName, folder) -> {
                String normalizeName = fullFolderName;
                if(!(fullFolderName.equals("/"))) {
                    normalizeName = fullFolderName.charAt(0) +
                            fullFolderName.substring(1)
                                    .replace(separator.mirror().pure(), separator.pure());
                }
                normalizeMapFile.put(normalizeName, folder);
            });

            return normalizeMapFile;
        }

        return mapFile;
    }

    private boolean folderIsExist(Name folderName) {
        if(!excludeNonExisting)
            return true;
        else {
            String fullFolderName = folderName.fullName();
            if(!fullFolderName.equals(new NameFolderRoot().fullName()))
                fullFolderName = fullFolderName.substring(2); // remove /\ in the beginning
            return (Files.exists(Paths.get(fullFolderName)));
        }
    }
}
