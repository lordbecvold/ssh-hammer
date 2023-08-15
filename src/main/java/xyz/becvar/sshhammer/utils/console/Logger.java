package xyz.becvar.sshhammer.utils.console;

import xyz.becvar.sshhammer.Main;

public enum Logger {

    INSTANCE;

    // main app prefix string
    public String prefix = ConsoleColors.CODES.ANSI_YELLOW + "[" + ConsoleColors.CODES.ANSI_GREEN + Main.APP_NAME + ConsoleColors.CODES.ANSI_YELLOW + "]" + ConsoleColors.CODES.ANSI_YELLOW + ": " + ConsoleColors.CODES.ANSI_CYAN;

    // log to console with custom app prefix
    public void log(String msg) {
        System.out.println(prefix + msg);
    }

    public void logError(String msg) {
        System.out.println(prefix + ConsoleColors.CODES.ANSI_RED + msg);
    }
}
