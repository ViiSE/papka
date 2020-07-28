package ru.viise.papka.entity;

import ru.viise.papka.system.Separator;
import ru.viise.papka.system.SeparatorUnix;

public class NamePure implements Name {

    private String pureFullName;
    private String pureShortName;
    private final String fullName;
    private final Separator separator;

    public NamePure(String fullName) {
        this(fullName, new SeparatorUnix());
    }

    public NamePure(String fullName, Separator separator) {
        this.fullName = fullName;
        this.separator = separator;
    }

    @Override
    public String shortName() {
        if(pureShortName == null)
            fullInit();

        return pureShortName;
    }

    @Override
    public String fullName() {
        if(pureFullName == null)
            fullInit();

        return pureFullName;
    }

    @Override
    public boolean equals(Object name) {
        if(!(name instanceof Name))
                return false;

        if(((Name) name).fullName().equals(this.fullName()))
            return true;

        return super.equals(name);
    }

    private String pure(String name) {
        return name
                .replace("*", "")
                .replace("?", "")
                .replace("\"", "")
                .replace("<", "")
                .replace(">", "")
                .replace("|", "")
                .replace("+", "")
                .replace("\u0000", "");
    }

    public String init() {
        String name = fullName.replace("//", "/"); // for root folder

        if(name.length() == 1)
            return name;

        if(name.charAt(name.length() - 1) == separator.charS())
            name = name.substring(0, name.length() - 1);
        if(name.charAt(name.length() - 1) == separator.mirror().charS())
            name = name.substring(0, name.length() - 1);
        name = name.charAt(0) + name.substring(1).replace(separator.mirror().pure(), separator.pure());
        return name;
    }

    public void fullInit() {
        String pureName = init();
        String[] names;

        if(pureName.length() == 1) {
            pureShortName = pureName;
            pureFullName = pureName;
        } else {
            names = pureName.split(separator.regex());
            pureShortName = pure(names[names.length - 1]);
            pureFullName = pure(pureName);
        }
    }
}
