package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FindFolderByShortName<T> implements FindFolder<T, String> {

    private final Folder<T> folder;
    private final boolean isLast;

    public FindFolderByShortName(Folder<T> folder) {
        this(folder, false);
    }

    public FindFolderByShortName(Folder<T> folder, boolean isLast) {
        this.folder = folder;
        this.isLast = isLast;
    }

    @Override
    public Folder<T> answer(String shortName) throws NotFoundException {
        if(!isLast) {
            if (folder.shortName().equals(shortName))
                return folder;
        }

        AtomicReference<Folder<T>> foundFolder = new AtomicReference<>();
        AtomicBoolean isEnough = new AtomicBoolean(false);

        folder.travel(folder -> {
            if(isLast) {
                if (folder.shortName().equals(shortName))
                    foundFolder.set(folder);
            } else if(!isEnough.get()) {
                if (folder.shortName().equals(shortName)) {
                    foundFolder.set(folder);
                    isEnough.set(true);
                }
            }
        });

        if(!isLast) {
            if (!isEnough.get())
                throw exception(shortName, folder.fullName());
        } else if(foundFolder.get() == null)
            throw exception(shortName, folder.fullName());

        return foundFolder.get();
    }

    private NotFoundException exception(String shortName, String fullName) {
        return new NotFoundException("Folder '" + shortName + "' not found in folder '" + fullName + "'.");
    }
}
