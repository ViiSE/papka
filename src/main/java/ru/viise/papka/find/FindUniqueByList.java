package ru.viise.papka.find;

import ru.viise.papka.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FindUniqueByList<T> implements Find<List<T>, List<T>> {

    @Override
    public List<T> answer(List<T> rawList) throws NotFoundException {
        List<T> unique = rawList.stream()
                .filter(e -> Collections.frequency(rawList, e) == 1)
                .distinct()
                .collect(Collectors.toList());

        if(unique.isEmpty())
            throw new NotFoundException("Unique values not found");

        return unique;
    }
}
