package ru.viise.papka.entity;

public class NamePure implements Name {

    private String pureFullName;
    private String pureShortName;
    private final String fullName;

    public NamePure(String fullName) {
        this.fullName = fullName;
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
                .replaceAll(":", "")
                .replaceAll("\\*", "")
                .replaceAll("\\?", "")
                .replaceAll("\"", "")
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll("\\|", "")
                .replaceAll("\\+", "")
                .replaceAll("\u0000", "");
    }

    public String init() {
        String name = fullName.replaceAll("//", "/");
        if(name.charAt(name.length() - 1) == '/')
            name = name.substring(0, name.length() - 1);
        return name;
    }

    public void fullInit() {
        String pureName = init();
        String[] names = pureName.split("/");
        pureShortName = pure(names[names.length - 1]);
        pureFullName = pure(pureName);
    }
}
