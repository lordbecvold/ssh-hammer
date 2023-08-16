package xyz.becvar.sshhammer.utils;

import xyz.becvar.sshhammer.utils.console.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    // init instances
    public static Logger logger = Logger.INSTANCE;

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

    // remove line from file
    public static void removeLineFromFile(String file, String lineToRemove) {
        try {
            File inFile = new File(file);

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            // construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            // read from the original file and write to the new
            // unless content matches data to be removed.
            while ((line = br.readLine()) != null) {

                if (!line.trim().equals(lineToRemove)) {

                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            // delete the original file
            if (!inFile.delete()) {
                logger.logError("Error: delete " + lineToRemove + " form " + file);
                return;
            }

            //R rname the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile)) {
                logger.logError("Error: rename file: " + file);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
