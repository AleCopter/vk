package com.aemmie.vk.auth;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.System.exit;

public class Auth {
    private static String accessToken;
    private static File TOKEN = new File("my.token");

    public static void init() {
        if (!TOKEN.exists()) {
            updateToken();
        }
        readTokenFromFile();
    }

    public static String getAccessToken() {
        return accessToken;
    }

    private static void setAccessToken(String accessToken) {
        Auth.accessToken = accessToken;
    }

    public static String getMyId() {
        return "118892922";
    }

    public static void updateToken() {
//        if (TOKEN.exists()) {
//            TOKEN.delete();
//        }
        StringBuilder output = new StringBuilder();
        try {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JTextField login = new JTextField();
            JTextField pass = new JTextField();
            panel.add(new JLabel("Login:"));
            panel.add(login);
            panel.add(new JLabel("Password:"));
            panel.add(pass);
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, panel, "Authentication", JOptionPane.OK_CANCEL_OPTION)) {
                Process proc = Runtime.getRuntime().exec("token\\php\\php.exe token\\cli\\vk-audio-token.php " + login.getText() + ' ' + pass.getText());

                while (proc.isAlive()) {
                    Thread.sleep(200);
                }

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(proc.getInputStream()));
                String s;
                while ((s = stdInput.readLine()) != null) {
                    output.append(s).append('\n');
                }
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(proc.getErrorStream()));
                while ((s = stdError.readLine()) != null) {
                    output.append(s).append('\n');
                }

            } else {
                exit(1);
            }
        } catch (Exception e) {
            exit(1);
        }
        if (!TOKEN.exists()) {
            JOptionPane.showMessageDialog(null, output.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            updateToken();
        } else {
            try {
                Scanner scan = new Scanner(TOKEN);
                setAccessToken(scan.nextLine());
                System.out.println(getAccessToken());
            } catch (FileNotFoundException e) {
                updateToken();
            }
        }
    }

    private static void readTokenFromFile() {
        try {
            Scanner scan = new Scanner(TOKEN);
            setAccessToken(scan.nextLine());
        } catch (FileNotFoundException e) {
            exit(1);
        }
    }

}
