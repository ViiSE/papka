package ru.viise.papka.entity;

import java.util.Arrays;
import java.util.List;

public class FoldersFileName implements FoldersFile<String, String> {

    @Override
    public List<String> folders(String fullFilename) {
        fullFilename = fullFilename.replace("//", "/");
        String[] foldersName = fullFilename.split("/");

        int length = foldersName.length - 1;
        if(foldersName[0].equals(""))
            foldersName[0] = "/";
        if(fullFilename.charAt(fullFilename.length() - 1) == '/')
            ++length;
        if(!(foldersName[0].equals("/"))) {
            foldersName = pushRoot(foldersName, length);
            length = foldersName.length;
        }

        return Arrays.asList(Arrays.copyOfRange(foldersName, 0, length));
    }

    private String[] pushRoot(String[] folders, int length) {
        String[] foldersWithRoot = new String[length + 1];
        foldersWithRoot[0] = "/";
        System.arraycopy(folders, 0, foldersWithRoot, 1, length);
        return foldersWithRoot;
    }
}
