package Server;

import java.io.*;
import java.net.Socket;

class listens implements Runnable {
    private Socket socket;
    private BufferedReader in = null;
    private String msg = "";
    private String welcome = "";
    public user[] U;
    private user users = null;

    public listens(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            welcome = "用户:" + this.socket.getInetAddress() + "~加入服务器 | "
                    + "当前在线人数:" + listen.getmList().size();
           // sendmsg(welcome);
            System.out.println(welcome);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if ((msg = in.readLine()) != null) {
                    if ("connect".equals(msg)) {  //网络连接
                      /*  Socket socket1 = listen.getmList().get(0);
                        Socket socket2 = listen.getmList().get(1);
                        Socket socket3 = new Socket(socket1.getInetAddress(), socket1.getPort(), socket2.getInetAddress(), socket2.getPort());
*/
                    } else if ("bye".equals(msg)) {
                        System.out.println("~~~~~~~~~~~~~");
                        listen.getmList().remove(this.socket);
                        welcome = "用户:" + this.socket.getInetAddress()
                                + "退出:" + "当前在线人数:" + listen.getmList().size();
                        System.out.println(welcome);
                        break;
                    } else {
                        String[] split = msg.split(",");//[ipv6,name, key]
                        users = new user(split[1], split[0], split[2]);
                        String flags = listen.database.checkName(users);

                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        bw.write(users.NickName + flags + "\n");
                        bw.flush();
                        log.write("向" + users.NickName +"发送登陆结果");
                        System.out.println(users.NickName + flags);
                        if ("连接服务器成功,用户已登录".equals(flags) || "连接服务器成功,用户已注册".equals(flags)) {

                            //发送用户表

                            //向服务器端发送一条消息
                            U = listen.database.selectAll();
                            bw.write(U.length + "\n");
                            bw.flush();
                            log.write("向" + users.NickName +"发送用户数");

                            String length = br.readLine();
                            log.write(users.NickName +"接收用户数" + length);

                            for (user user : U) {
                                bw.write(user.id + "," + user.NickName + "," + user.IPv6 + "," + user.key + "," + user.status + "\n");
                            }
                            bw.flush();
                            log.write("向" + users.NickName +"发送用户表");

                            String s = br.readLine();
                            log.write(users.NickName +"接收用户表" + s);

                            //读取服务器返回的消息
                            String mess = br.readLine();
                            System.out.println("接收用户" + users.NickName +"消息:" + mess);
                            log.write("接收用户" + users.NickName +"消息:" + mess);


                        } else {
                            System.out.println("~~~~~~~~~~~~~");
                            listen.getmList().remove(this.socket);
                            welcome = "用户:" + this.socket.getInetAddress()
                                    + "退出:" + "当前在线人数:" + listen.getmList().size();
                            System.out.println(welcome);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("~~~~~~~~~~~~~");
            listen.getmList().remove(this.socket);
            welcome = "用户:" + users.NickName + "(" +this.socket.getInetAddress() + ")"
                    + "退出:" + "当前在线人数:" + listen.getmList().size();
            System.out.println(e.getMessage());
            System.out.println(welcome);
            listen.getmList().remove(socket);
        }
    }





}

