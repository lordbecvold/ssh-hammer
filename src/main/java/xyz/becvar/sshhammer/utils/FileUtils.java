package xyz.becvar.sshhammer.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    // check if file exist
    public static boolean ifFileExist(String name) {
        File fileOBJ = new File(name);
        if (fileOBJ.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // write to file
    public static void saveMessageLog(String line, String file) {
        try {
            if (Files.notExists(Paths.get(file))) {
                File f = new File(String.valueOf(Paths.get(file)));
                f.createNewFile();
            }
            try(PrintWriter output = new PrintWriter(new FileWriter(file,true))) {
                output.printf("%s\r\n",line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
