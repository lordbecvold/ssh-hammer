package xyz.becvar.sshhammer.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import xyz.becvar.sshhammer.utils.console.Logger;
import java.io.IOException;
import java.net.*;

public class NetworkUtils {

    // check if adress reachable and return boolean value
    public static boolean isReachable(String host, int openPort, int timeOutMillis) {
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(host, openPort), timeOutMillis);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    // check if string is ip adress
    public static final boolean checkIPv4(final String ip) {
        boolean isIPv4;
        try {
            final InetAddress inet = InetAddress.getByName(ip);
            isIPv4 = inet.getHostAddress().equals(ip)
                    && inet instanceof Inet4Address;
        } catch (final UnknownHostException e) {
            isIPv4 = false;
        }
        return isIPv4;
    }

    // check if connection valid
    public static boolean sshConnectionValid(String username, int port, String host, String password) {
        try {
            // init jsch
            JSch jsch = new JSch();

            // create session
            Session session = jsch.getSession(username, host, port);

            // set session configs
            //session.setConfig("PreferredAuthentications", "password");
            session.setConfig("StrictHostKeyChecking", "no");

            // set session password
            session.setPassword(password);

            // print log
            Logger.INSTANCE.log("Trying to connect ssh host: " + host + ":" + port + " with " + username + ":" + password);

            // try conneect to session
            session.connect();

            // check if session connected
            if (session.isConnected()) {
                return true;
            } else {
                return false;
            }

        } catch (JSchException e) {

            // print connction error
            Logger.INSTANCE.logError("host: " + host + ":" + port + " -> " + username + ":" + password + " connection error " + e.getMessage());
            return false;
        }
    }
}
