import java.net.*;
import java.io.*;

public class UnlimitedServer {
    public void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(777);
            while (true) {
                Socket socket = server.accept();
                ServiceEcho serv = new ServiceEcho(socket);
                Thread t = new Thread(serv);
                t.start();
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}