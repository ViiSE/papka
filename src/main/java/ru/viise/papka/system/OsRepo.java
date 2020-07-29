/*
 * Copyright 2020 ViiSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.viise.papka.system;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class OsRepo implements OsRepository {

    private final Map<String, Os> osMap = new HashMap<>();

    @Override
    public Os instance() {
        if(osMap.isEmpty())
            init();

        AtomicReference<Os> currentOs = new AtomicReference<>();
        osMap.forEach((name, os) -> {
            if(System.getProperty("os.name").toLowerCase().contains(name))
                currentOs.set(os);
        });

        return currentOs.get();
    }

    private void init() {
        osMap.put("windows", new OsWindows());
        osMap.put("linux", new OsUnixLike());
        osMap.put("mac", new OsUnixLike());
    }
}
