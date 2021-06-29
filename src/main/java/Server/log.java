package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: log <br>
 * date: 2020/12/23 10:35 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class log {
    static FileWriter flog;
    public static void Serverstart() throws IOException {
        flog = new FileWriter("Serverlog.txt",true);
    }
    public static void write(String items) throws IOException {

        flog.write(items + " : " + new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        flog.flush();
    }
    public static void close() throws IOException {
        flog.close();
    }

}
