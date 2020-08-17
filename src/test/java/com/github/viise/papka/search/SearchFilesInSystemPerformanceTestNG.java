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

package com.github.viise.papka.search;

import com.github.viise.papka.exception.NotFoundException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static utils.PrintTest.prettyRowCol;
import static utils.PrintTest.testTime;

public class SearchFilesInSystemPerformanceTestNG {

    private int amountTests = 1000;
    private int charColCount = 21;

    private List<TimeResult> resSearch1;
    private List<TimeResult> resSearch2;
    private List<TimeResult> resSearch3;

    private Search<List<File>, String> search1;
    private Search<List<File>, String> search2;
    private Search<List<File>, String> search3;

    @BeforeClass
    public void beforeClass() {
        String beginWith = "/home/viise";

        search1 = new SearchFilesInSystem(beginWith);
        search2 = new SearchFilesInSystemIgnoreEx(beginWith);
        search3 = new SearchFilesInSystemParallel(beginWith);

        resSearch1 = new ArrayList<>();
        resSearch2 = new ArrayList<>();
        resSearch3 = new ArrayList<>();
    }

    @Test
    public void test() {
        System.out.println("TEST");
        System.out.println("1) SearchFilesInSystem | 2) *IgnoreEx | 3) *Parallel");
        System.out.println("+---------------------+---------------------+---------------------+");
        System.out.println("|          1          |          2          |          3          |");
        System.out.println("+---------------------+---------------------+---------------------+");

        for(int i = 0; i < amountTests; i ++) {
            String res1 = result(search1, resSearch1, "SearchFilesInSystem");
            String res2 = result(search2, resSearch2, "*IgnoreEx");
            String res3 = result(search3, resSearch3, "*Parallel");

            System.out.print(prettyRowCol(res1, charColCount, true));
            System.out.print(prettyRowCol(res2, charColCount, false));
            System.out.print(prettyRowCol(res3, charColCount, false));
            System.out.println();
            System.out.println("+---------------------+---------------------+---------------------+");
        }

        System.out.println("|                              TOTAL                              |");
        System.out.println("+---------------------+---------------------+---------------------+");

        String totalRes1 = testTime(getTotalTimeMills(resSearch1));
        String totalRes2 = testTime(getTotalTimeMills(resSearch2));
        String totalRes3 = testTime(getTotalTimeMills(resSearch3));

        System.out.print(prettyRowCol(totalRes1, charColCount, true));
        System.out.print(prettyRowCol(totalRes2, charColCount, false));
        System.out.print(prettyRowCol(totalRes3, charColCount, false));
        System.out.println();


        System.out.println("+---------------------+---------------------+---------------------+");
        System.out.println("|                             AVERAGE                             |");
        System.out.println("+---------------------+---------------------+---------------------+");

        String avgRes1 = testTime(getAverageTimeMills(getTotalTimeMills(resSearch1)));
        String avgRes2 = testTime(getAverageTimeMills(getTotalTimeMills(resSearch2)));
        String avgRes3 = testTime(getAverageTimeMills(getTotalTimeMills(resSearch3)));

        System.out.print(prettyRowCol(avgRes1, charColCount, true));
        System.out.print(prettyRowCol(avgRes2, charColCount, false));
        System.out.print(prettyRowCol(avgRes3, charColCount, false));
        System.out.println();
        System.out.println("+---------------------+---------------------+---------------------+");

        System.out.println("Test end");
    }

    private String result(Search<List<File>, String> search, List<TimeResult> searchResList, String className) {
        try {
            String pattern = ".*";

            long startMills = System.currentTimeMillis();
            search.answer(pattern);
            long endMills = System.currentTimeMillis();
            searchResList.add(new TimeResult(endMills - startMills));

            return testTime(startMills, endMills);
        } catch (NotFoundException e) {
            System.out.println(className + " EXCEPTION - " + e.getMessage());
            return "N/A";
        }
    }

    private long getTotalTimeMills(List<TimeResult> listResult) {
        long total = 0L;
        for(TimeResult timeResult: listResult) {
            total += timeResult.mills;
        }

        return total;
    }

    private long getAverageTimeMills(long total) {
        return total / amountTests;
    }

    private static class TimeResult {

        private final long mills;

        public TimeResult(long mills) {
            this.mills = mills;
        }
    }
}
