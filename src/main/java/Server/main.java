package Server;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        log.Serverstart();
        config.loadConfig();
        listen.listen();
    }
}
