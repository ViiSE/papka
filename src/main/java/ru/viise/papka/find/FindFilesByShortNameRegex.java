package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindFilesByShortNameRegex implements Find<List<File>, String> {

    private final boolean includeChildren;
    private final Folder<File> folder;

    public FindFilesByShortNameRegex(Folder<File> folder, boolean includeChildren) {
        this.folder = folder;
        this.includeChildren = includeChildren;
    }

    public FindFilesByShortNameRegex(Folder<File> folder) {
        this(folder, false);
    }

    @Override
    public List<File> answer(String regex) throws NotFoundException {
        List<File> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        if (includeChildren)
            folder.travel(folder -> files.addAll(findByShort(pattern, folder)));
        else
            files.addAll(findByShort(pattern, folder));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<File> findByShort(Pattern pattern, Folder<File> folder) {
        List<File> files = new ArrayList<>();

        for(File file: folder.files()) {
            if(pattern.matcher(file.getName()).matches()) {
                files.add(file);
            }
        }

        return files;
    }
}
