package ru.viise.papka.find;

import ru.viise.papka.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FindDuplicatesByList<T> implements Find<List<T>, List<T>> {

    @Override
    public List<T> answer(List<T> rawList) throws NotFoundException {
        List<T> duplicates = rawList.stream()
                .filter(e -> Collections.frequency(rawList, e) > 1)
                .distinct()
                .collect(Collectors.toList());

        if(duplicates.isEmpty())
            throw new NotFoundException("Duplicates not found.");

        return duplicates;
    }
}
