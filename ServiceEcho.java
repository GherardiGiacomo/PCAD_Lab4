import java.io.*;
import java.net.*;
import java.lang.*;

public class ServiceEcho implements Runnable {
    public Socket socket;

    public ServiceEcho(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String mess = br.readLine();
            System.out.println("Receuve message: " + mess);
            pw.println("Echo: " + mess);
            pw.flush();
            br.close();
            pw.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
