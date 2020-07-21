package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindFilesByFolderNameRegex<T> implements Find<List<T>, String> {

    private final Folder<T> folder;
    private final boolean isFullName;

    public FindFilesByFolderNameRegex(
            Folder<T> folder,
            boolean isFullName) {
        this.folder = folder;
        this.isFullName = isFullName;
    }

    public FindFilesByFolderNameRegex(Folder<T> folder) {
        this(folder, true);
    }

    @Override
    public List<T> answer(String regex) throws NotFoundException {
        List<T> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        folder.travel(folder -> files.addAll(findFolder(pattern, folder)));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<T> findFolder(Pattern pattern, Folder<T> folder) {
        if (isFullName)
            return matchesName(pattern, folder, folder.fullName());
        else
            return matchesName(pattern, folder, folder.shortName());
    }

    private List<T> matchesName(Pattern pattern, Folder<T> folder, String name) {
        if (pattern.matcher(name).matches())
            return folder.files();
        return new ArrayList<>();
    }
}
