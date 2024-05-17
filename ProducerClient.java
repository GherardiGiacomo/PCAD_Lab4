import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ProducerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 777;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("producer");
            writer.flush();

            String response = reader.readLine();
            System.out.println("Risposta dal server: " + response);

            writer.println("producer\n");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}