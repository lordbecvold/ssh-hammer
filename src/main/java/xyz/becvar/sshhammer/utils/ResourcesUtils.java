package xyz.becvar.sshhammer.utils;

import xyz.becvar.sshhammer.utils.console.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourcesUtils {

    // init instances
    public static Logger logger = Logger.INSTANCE;

    // function for copy resource to working directory
    public static void copyResource(InputStream source, String destination) {
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
            logger.log("file: " + destination + " created!");
        } catch (IOException e) {
            logger.logError("error create file: " + destination);
            SystemUtils.appShutdown(0);
        }
    }
}
