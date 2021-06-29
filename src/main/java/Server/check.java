package Server;

import java.util.Date;

/**
 * description: 每5分钟遍历一次,更新状态 <br>
 * date: 2020/12/18 14:30 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class check extends Thread {
    public void run() {
        try {
            while (true) {
                Date date = new Date();
                long nowTime = date.getTime();
                user[] U = listen.database.selectAll();
                for (user users : U) {
                    if (users.timeStramp.getTime() + 300000 < nowTime) { //上线时间+5min <当前时间
                        if (users.status = true) {
                            users.status = false;
                            listen.database.updatestate(users.NickName, false);
                        }
                    }
                }
                sleep(60000);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
