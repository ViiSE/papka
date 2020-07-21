package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FindFolderByFullName<T> implements FindFolder<T, String> {

    private final Folder<T> folder;

    public FindFolderByFullName(Folder<T> folder) {
        this.folder = folder;
    }

    @Override
    public Folder<T> answer(String fullName) throws NotFoundException {
        if(folder.fullName().equals(fullName))
            return folder;

        AtomicReference<Folder<T>> foundFolder = new AtomicReference<>();
        AtomicBoolean isEnough = new AtomicBoolean(false);

        folder.travel(folder -> {
            if(!isEnough.get()) {
                if (folder.fullName().equals(fullName)) {
                    foundFolder.set(folder);
                    isEnough.set(true);
                }
            }
        });

        if(!isEnough.get())
            throw new NotFoundException("Folder '" + fullName + "' not found in folder '" + folder.fullName() + "'.");

        return foundFolder.get();
    }
}
