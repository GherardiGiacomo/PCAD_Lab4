import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConsumerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 777;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("consumer");
            writer.flush(); //aggiunto dal prof

            String response = reader.readLine();
            System.out.println("Risposta dal server: " + response);

            writer.println("consumer\n");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
