package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindFilesByRegex implements Find<List<String>, String> {

    private final boolean includeChildren;
    private final Folder<String> folder;

    public FindFilesByRegex(Folder<String> folder, boolean includeChildren) {
        this.folder = folder;
        this.includeChildren = includeChildren;
    }

    public FindFilesByRegex(Folder<String> folder) {
        this(folder, false);
    }

    @Override
    public List<String> answer(String regex) throws NotFoundException {
        List<String> files = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        if (includeChildren)
            folder.travel(folder -> files.addAll(find(pattern, folder)));
        else
            files.addAll(find(pattern, folder));

        if (files.isEmpty())
            throw new NotFoundException("Folder matches regex '" + regex + "' not contains files.");
        else
            return files;
    }

    private List<String> find(Pattern pattern, Folder<String> folder) {
        List<String> files = new ArrayList<>();

        for(String file: folder.files()) {
            if(pattern.matcher(file).matches()) {
                files.add(file);
            }
        }

        return files;
    }
}
