package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class listen {
    public static dataBase database = new dataBase();
    private static int port = config.listenport;  //监听端口
    private static ServerSocket server;
    private static ExecutorService myExecutorService;
    private static List<Socket> mList = new ArrayList();


    public static void listen(){
        try
        {
            server = new ServerSocket(port);
            //创建线程池
            myExecutorService = Executors.newCachedThreadPool();

            Socket client;
            database.connect();
            new check().start();
            System.out.println("服务端运行中...\n");
            while(true)
            {
                client = server.accept();
                mList.add(client);
                myExecutorService.execute(new listens(client));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<Socket> getmList(){
        return mList;
    }

}
