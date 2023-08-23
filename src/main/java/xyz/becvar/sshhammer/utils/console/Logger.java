package xyz.becvar.sshhammer.utils.console;

import xyz.becvar.sshhammer.Main;

public enum Logger {

    INSTANCE;

    // log ascii logo
    public void logAscii() {
        System.out.print(ConsoleColors.CODES.ANSI_GREEN + "███████╗███████╗██╗  ██╗      ██╗  ██╗ █████╗ ███╗   ███╗███╗   ███╗███████╗██████╗ \n" +
                "██╔════╝██╔════╝██║  ██║      ██║  ██║██╔══██╗████╗ ████║████╗ ████║██╔════╝██╔══██╗\n" +
                "███████╗███████╗███████║█████╗███████║███████║██╔████╔██║██╔████╔██║█████╗  ██████╔╝\n" +
                "╚════██║╚════██║██╔══██║╚════╝██╔══██║██╔══██║██║╚██╔╝██║██║╚██╔╝██║██╔══╝  ██╔══██╗\n" +
                "███████║███████║██║  ██║      ██║  ██║██║  ██║██║ ╚═╝ ██║██║ ╚═╝ ██║███████╗██║  ██║\n" +
                "╚══════╝╚══════╝╚═╝  ╚═╝      ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═╝\n" +
                "                                                                                    " + "\n");
    }

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

    // log error to console
    public void logError(String msg) {
        System.out.println(prefix + ConsoleColors.CODES.ANSI_RED + msg);
    }
}
