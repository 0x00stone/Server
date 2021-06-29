package Server;

import java.sql.*;
import java.util.Date;

/**
 * description: 数据库相关操作 <br>
 * date: 2020/11/24 14:11 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class dataBase {
    private String url = "jdbc:mysql://localhost:3306/chat?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private String user = config.sqlUsername;
    private String password = config.sqlPassword;
    private Statement statement;

    /*
     * description: 连接数据库
     * version: 1.0
     * date: 2020/11/24 14:27
     * author: Revers.
     *
     * @param
     * @return void
     */
    void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user,password);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            log.write("连接数据库");
            System.out.println("数据库连接成功...\n");
        }
        catch (Exception e){
            System.out.println("连接数据库失败");
            e.printStackTrace();
        }
        return ;
    }

    boolean addpers(user users) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT MAX(id) FROM chat.db");
        int id = 0;
        if(rs.next()){
            id = rs.getInt("MAX(id)");
        }
        id++;
        boolean bool = statement.execute("INSERT INTO chat.db(id,用户名,ipv6,`key`, `status`,lastTime) VALUES ("+ id +",'"+ users.NickName +"','"+ users.IPv6 +"','"+ users.key +"',b'01','"+ users.timeStramp +"');");
        return bool;
    }


    user[] selectAll() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM chat.db;");

        int rowCount = 0; //得到当前行号，也就是记录数
        while(resultSet.next()){
            rowCount++;
        }
        resultSet.first();
        user[] users = new user[rowCount];
        int i = 0;
        if(rowCount > 0) {
            do {
                users[i] = new user(resultSet.getInt("id"), resultSet.getString("用户名"), resultSet.getString("ipv6"),
                        resultSet.getString("key"), resultSet.getBoolean("status"), resultSet.getTimestamp("lastTime"));
                i++;
            } while (resultSet.next());
        }
        return  users;
    }

    String selectKeyByName(String name) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM chat.db WHERE 用户名= '"+name+"'");
        String ip = resultSet.getString(3);
        return ip;
    }

    //修改IP
    int updateip(String name,String changeString) throws SQLException {
        int i = statement.executeUpdate("UPDATE chat.db SET ipv6 = '"+changeString+"' WHERE 用户名 = '"+name+"';");
        return i;//应该只返回一行,return 1 or 0;
    }

    //修改状态
    int updatestate(String name,boolean changeString) throws SQLException {
        int i;
        if(changeString == true){
            i = statement.executeUpdate("UPDATE chat.db SET status = b'01' WHERE 用户名 = '"+name+"';");
        }else {
            i = statement.executeUpdate("UPDATE chat.db SET status = b'00' WHERE 用户名 = '"+name+"';");
        }
        return i;//应该只返回一行,return 1 or 0;
    }

    void updateTime(user users) throws SQLException {
        Timestamp nowTime = new Timestamp(new Date().getTime());
        statement.executeUpdate("UPDATE chat.db SET lastTime = '"+ nowTime +"' WHERE 用户名 = '"+users.NickName+"';");
    }

    void login(user users) throws SQLException {
        updateip(users.NickName,users.IPv6);
        updatestate(users.NickName,true);
        updateTime(users);
    }

    int offline(user users) throws SQLException {
        return updatestate(users.NickName,false);
    }

    //一个公钥最多5个用户
    //一个用户名只能一个用户
    String checkName(user users) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select * from chat.db WHERE `key` = '" + users.key + "'");
        resultSet.last();
        int rowCount = resultSet.getRow(); //得到当前行号，也就是记录数

        resultSet.first();
        boolean HasName = false; //相同公钥是否有唯一用户名
        if(rowCount>5){
            return "该公钥绑定用户名数量过多";
        }else if(rowCount == 0) {
               ;
        }else{
            do{
                String name = resultSet.getString(2);
                if(users.NickName.equals(name)){
                    HasName = true;
                }
            }while (resultSet.next());
        }

        ResultSet resultSet2 = statement.executeQuery("select * from chat.db WHERE 用户名 = '" + users.NickName + "'");
        resultSet2.last();
        int row = resultSet2.getRow();
        if(row > 1){
            return "用户名重复";
        }else if(row ==1) {
            if (!users.key.equals(resultSet2.getString(4))) {
                return "用户名重复";
            }
        }

        if(HasName){
            //登录上线
            login(users);
            return "连接服务器成功,用户已登录";
        }else{
            if(rowCount >= 5){
                return "该公钥绑定用户名数量过多";
            }
            addpers(users);//创建
            return "连接服务器成功,用户已注册";
        }
    }






}
