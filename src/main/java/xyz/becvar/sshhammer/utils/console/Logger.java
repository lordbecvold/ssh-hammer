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

    // log to console with custom app prefix in line
    public void logInLine(String msg) {
        System.out.print(prefix + msg);
    }

    public void logError(String msg) {
        System.out.println(prefix + ConsoleColors.CODES.ANSI_RED + msg);
    }

    public void printSpacer() {
        System.out.println(ConsoleColors.CODES.ANSI_WHITE + "=====================================================================================================");
    }
}
