package ru.viise.papka.system;

public class OsImpl implements Os {

    @Override
    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    @Override
    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    @Override
    public boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
