package Server;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * description: user <br>
 * date: 2020/11/24 14:43 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class user {
    public int id;
    public String NickName;
    public String IPv6;
    public String key;

    public boolean status;
    public Timestamp timeStramp;

    user(int id,String name,String IPv6 ,String key) throws SQLException {
        this.id = id;
        this.NickName = name;
        this.IPv6 = IPv6;
        this.key = key;
        this.status = false;
    }
    user(String name,String IPv6,String key) throws SQLException {
        this.NickName = name;
        this.IPv6 = IPv6;
        this.key = key;
        this.status = false;
        timeStramp = new Timestamp(new Date().getTime());
    }
    user(int id,String name,String ipv6,String key,boolean status,Timestamp timeStramp) throws SQLException {
        this.id = id;
        this.NickName = name;
        this.IPv6 = ipv6;
        this.key = key;
        this.status = status;
        this.timeStramp = timeStramp;
    }

    public boolean changeName(String name,String key,dataBase database) throws SQLException {
        String s = database.selectKeyByName(name);
        if(s == key){
            return true;
        }else{
            return false;
        }
    }
}
