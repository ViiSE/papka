package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindFoldersByRegex<T> implements FindFolders<T, String> {

    private final Folder<T> folder;
    private final boolean isFullName;

    public FindFoldersByRegex(Folder<T> folder, boolean isFullName) {
        this.folder = folder;
        this.isFullName = isFullName;
    }

    public FindFoldersByRegex(Folder<T> folder) {
        this(folder, true);
    }

    @Override
    public List<Folder<T>> answer(String regex) throws NotFoundException {
        List<Folder<T>> folders = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        folder.travel(folder -> {
            try {
                folders.add(findFolder(pattern, folder));
            } catch (NotFoundException ignore) {}
        });

        if(folders.isEmpty())
            throw new NotFoundException("Folders matches regex '" + regex + "' not found in folder " + folder.fullName() + ".");
        else
            return folders;
    }

    private Folder<T> findFolder(Pattern pattern, Folder<T> child) throws NotFoundException {
        String name = child.fullName();
        if(!isFullName)
            name = child.shortName();
        if(pattern.matcher(name).matches())
            return child;

        throw new NotFoundException("Not found. Ignore");
    }
}
