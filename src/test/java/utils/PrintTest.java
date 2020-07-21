package utils;

import ru.viise.papka.entity.Folder;

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
}
