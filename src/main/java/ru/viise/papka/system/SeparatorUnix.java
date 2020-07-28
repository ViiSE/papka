package ru.viise.papka.system;

public class SeparatorUnix implements Separator {

    @Override
    public String pure() {
        return "/";
    }

    @Override
    public char charS() {
        return '/';
    }

    @Override
    public String regex() {
        return "/";
    }

    @Override
    public Separator mirror() {
        return new SeparatorWin();
    }
}
