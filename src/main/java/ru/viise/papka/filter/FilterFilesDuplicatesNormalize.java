package ru.viise.papka.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterFilesDuplicatesNormalize<T> implements Filter<List<T>> {

    private final List<T> rawFiles;

    public FilterFilesDuplicatesNormalize(List<T> rawFiles) {
        this.rawFiles = rawFiles;
    }

    @Override
    public List<T> apply() {
        List<T> readyFiles = new ArrayList<>();
        for(int i = 0; i < rawFiles.size(); i++) {
            T file = rawFiles.get(i);
            boolean isAdd = true;
            for(int j = 0; j < rawFiles.size(); j++) {
                if(i != j)
                    if(file.equals(rawFiles.get(j)))
                        if(readyFiles.contains(file)) {
                            isAdd = false;
                        }
            }

            if(isAdd)
                readyFiles.add(file);
        }

        return readyFiles;
    }
}
