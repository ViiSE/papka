package ru.viise.papka.find;

import ru.viise.papka.entity.Folder;
import ru.viise.papka.exception.NotFoundException;

import java.util.List;

public class FindFolderChildByShortName<T> implements FindFolder<T, String> {

    private final List<Folder<T>> children;

    public FindFolderChildByShortName(List<Folder<T>> children) {
        this.children = children;
    }

    @Override
    public Folder<T> answer(String shortName) throws NotFoundException {
        for(Folder<T> child: children) {
            if(child.shortName().equals(shortName))
                return child;
        }

        throw new NotFoundException("Folder '" + shortName + "' not found in children list.");
    }
}
