package ru.viise.papka.entity;

import ru.viise.papka.exception.NotFoundException;
import ru.viise.papka.find.FindFolderChildByShortName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TreeFolderPure<T> implements TreeFolder<T> {

    private final FoldersFile<String, String> foldersFile;
    private final PreparedFolders<Map<String, Folder<T>>> prepFolders;

    public TreeFolderPure(PreparedFolders<Map<String, Folder<T>>> prepFolders) {
        this.foldersFile = new FoldersFileName();
        this.prepFolders = prepFolders;
    }

    public TreeFolderPure(
            FoldersFile<String, String> foldersFile,
            PreparedFolders<Map<String, Folder<T>>> prepFolders) {
        this.foldersFile = foldersFile;
        this.prepFolders = prepFolders;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Folder<T> grow() {
        Map<String, Folder<T>> folderMap = prepFolders.preparation();
        Folder<T> root = (Folder<T>) folderMap.values().toArray()[0];

        folderMap.remove(root.fullName());
        AtomicReference<Folder<T>> prevFolder = new AtomicReference<>(root);

        folderMap.forEach((fullName, folder) -> {
            if (folder.shortName().equals(fullName.substring(1, fullName.length() - 1))) {
                root.children().add(folder);
            } else {
                List<String> foldersNames = foldersFile.folders(fullName);
                foldersNames = foldersNames.subList(1, foldersNames.size());
                addFolder(root.children(), folder, foldersNames);
            }
            prevFolder.set(folder);
        });

        return root;
    }

    private void addFolder(List<Folder<T>> rootChildren, Folder<T> folder, List<String> foldersName) {
        // First folder is root child
        Folder<T> root;
        try {
            root = new FindFolderChildByShortName<>(rootChildren).answer(foldersName.get(0));
        } catch (NotFoundException ex) {
            root = new FolderPure<>("/" + foldersName.get(0), new ArrayList<>());
        }

        String fullName = root.fullName();
        for (int i = 1; i < foldersName.size(); i++) {
            String shortName = foldersName.get(i);
            fullName += "/" + foldersName.get(i);
            try {
                root = new FindFolderChildByShortName<>(root.children()).answer(shortName);
            } catch (NotFoundException ex) {
                Folder<T> child = new FolderPure<>(fullName, new ArrayList<>());
                root.children().add(child);
                root = child;
            }
        }

        // Last folder is target child
        root.files().addAll(folder.files());
    }
}
