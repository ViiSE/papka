package ru.viise.papka.system;

public class CurrentDirectory implements Directory<String> {

    private final Separator separator;

    public CurrentDirectory(Separator separator) {
        this.separator = separator;
    }

    @Override
    public String name() {
        return System.getProperty("user.dir").replace(separator.mirror().pure(), separator.pure()) + separator.pure();
    }
}
