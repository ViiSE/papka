package ru.viise.papka.system;

public class ExampleDirectory implements Directory<String> {

    private final Directory<String> curDir;
    private final Separator separator;

    public ExampleDirectory(Directory<String> curDir, Separator separator) {
        this.curDir = curDir;
        this.separator = separator;
    }

    @Override
    public String name() {
        return (curDir.name() + "example" + separator.pure())
                .replace(separator.mirror().pure(), separator.pure());
    }
}
