import java.net.*;
import java.io.*;

public class Producer {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 777);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            pw.println("Hello, Server!");
            pw.flush();
            String mess = br.readLine();
            System.out.println("Server says: " + mess);
            pw.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
