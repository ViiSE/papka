package utils;

import com.github.viise.papka.entity.Folder;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintTest {

    public static void printFolder(Folder<String> folder) {
        System.out.println("Full name: " + folder.fullName());
        System.out.println("Short name: " + folder.shortName());
        System.out.println("Files: ");
        System.out.println("+_____________________________________________+");
        folder.files().forEach(fName -> {
            int whitespaceCount = 45 - fName.length();
            if(whitespaceCount < 0)
                System.out.print("|" + fName);
            else {
                System.out.print("|" + fName);
                for(int i = 0; i < whitespaceCount; i++)
                    System.out.print(" ");
                System.out.println("|");
            }
        });
        System.out.println("+_____________________________________________+");
    }

    public static void printFolderFile(Folder<File> folder) {
        System.out.println("Full name: " + folder.fullName());
        System.out.println("Short name: " + folder.shortName());
        System.out.println("Files: ");
        System.out.println("+_____________________________________________+");
        folder.files().forEach(file -> {
            String fName = file.getName();
            int whitespaceCount = 45 - fName.length();
            if(whitespaceCount < 0)
                System.out.print("|" + fName);
            else {
                System.out.print("|" + fName);
                for(int i = 0; i < whitespaceCount; i++)
                    System.out.print(" ");
                System.out.println("|");
            }
        });
        System.out.println("+_____________________________________________+");
    }

    public static void printTestTime(ITestResult tr) {
        System.out.println(testTime(tr));
    }

    public static void printTestTime(long startMills, long endMills) {
        System.out.println(testTime(startMills, endMills));
    }

    public static String testTime(ITestResult tr) {
        long milliseconds = tr.getEndMillis() - tr.getStartMillis();
        return new SimpleDateFormat("''mm'm' ss's' SSS'ms'").format(new Date(milliseconds));
    }

    public static String testTime(long startMills, long endMills) {
        long milliseconds = endMills - startMills;
        return new SimpleDateFormat("''mm'm' ss's' SSS'ms'").format(new Date(milliseconds));
    }

    public static String testTime(long totalMills) {
        return new SimpleDateFormat("''mm'm' ss's' SSS'ms'").format(new Date(totalMills));
    }

    public static String prettyRowCol(String result, int charColCount, boolean drawFirst) {
        int spaceCount = charColCount - result.length();
        if(spaceCount < 0)
            spaceCount = 0;

        StringBuilder prettyRes = new StringBuilder();

        if(drawFirst)
            prettyRes.append("| ").append(result);
        else
            prettyRes.append(" ").append(result);
        for(int i = 0; i < spaceCount - 1; i++) {
            prettyRes.append(" ");
        }
        prettyRes.append("|");

        return prettyRes.toString();
    }
}
