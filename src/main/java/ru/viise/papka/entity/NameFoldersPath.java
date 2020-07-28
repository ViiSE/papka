package ru.viise.papka.entity;

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;

import java.util.List;

public class NameFoldersPath implements Name {

    private String fullName = "";
    private String shortName = "";
    private final Separator separator;
    private final List<String> foldersName;

    public NameFoldersPath(List<String> foldersName, Separator separator) {
        this.separator = separator;
        this.foldersName = foldersName;
    }

    public NameFoldersPath(List<String> foldersName) {
        this(foldersName, new SeparatorUnix());
    }

    @Override
    public String shortName() {
        if(shortName.isEmpty())
            shortName = _shortName();
        return shortName;
    }

    @Override
    public String fullName() {
        if(fullName.isEmpty())
            fullName = _fullName();
        return fullName;
    }

    private String _fullName() {
        String fullName = String.join(separator.pure(), foldersName) + separator.pure();
        return fullName.replace(separator.pure() + separator.pure(), separator.pure());
    }

    private String _shortName() {
        return foldersName.get(foldersName.size() - 1);
    }
}
