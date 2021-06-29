package Server;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

/**
 * description: properties <br>
 * date: 2021/5/30 11:06 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class config {
    static String sqlUsername;
    static String sqlPassword;
    static int listenport;
    public static void saveConfig(String key,String value) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(new File("./serverConfig.properties"));
            Properties config = new Properties();
            config.setProperty(key,value);
            config.store(fos, "config");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.write(e.getMessage());
        }
    }


    public static void loadConfig() throws IOException {
        try {
            if(new File("./serverConfig.properties").exists()) {
                FileInputStream fis = new FileInputStream(new File("./serverConfig.properties"));
                Properties config = new Properties();
                config.load(fis);
                Set<String> keys = config.stringPropertyNames();

                for (String u : keys) {
                    if ("sqlUsername".equals(u)) {
                        sqlUsername = config.getProperty("sqlUsername", "");
                    } else if ("sqlPassword".equals(u)) {
                        sqlPassword = config.getProperty("sqlPassword", "");
                    } else if ("port".equals(u)) {
                        listenport = Integer.valueOf(config.getProperty("port", "0"));
                    }
                }
                if (sqlPassword == null || sqlPassword == null) {
                    System.out.println("请输入MySql用户名:");
                    Scanner scanner = new Scanner(System.in);
                    String username = scanner.nextLine();
                    saveConfig("sqlUsername", username);
                    sqlUsername = username;
                    System.out.println("请输入" + username + "的密码:");
                    String password = scanner.nextLine();
                    saveConfig("sqlPassword", password);
                    sqlPassword = password;
                }
                if ("0".equals(listenport)) {
                    System.out.println("请输入服务端监听端口:");
                    Scanner scanner = new Scanner(System.in);
                    int port = scanner.nextInt();
                    saveConfig("port", String.valueOf(port));
                    listenport = port;
                }
            }else{
                Scanner scanner = new Scanner(System.in);
                FileOutputStream fos = new FileOutputStream(new File("./serverConfig.properties"));
                Properties config = new Properties();


                System.out.println("请输入MySql用户名:");
                String username = scanner.nextLine();
                sqlUsername = username;
                config.setProperty("sqlUsername",username);

                System.out.println("请输入" + username + "的密码:");
                String password = scanner.nextLine();
                sqlPassword = password;
                config.setProperty("sqlPassword",password);

                System.out.println("请输入服务端监听端口:");
                int port = scanner.nextInt();
                listenport = port;
                config.setProperty("port",String.valueOf(port));

                config.store(fos, "config");
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.write(e.getMessage());
        }
    }
}

