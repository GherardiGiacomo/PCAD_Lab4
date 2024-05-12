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

            // Invia il tipo di client al server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("consumer");

            // Ricevi la stringa dal server
            String response = reader.readLine();
            System.out.println("Stringa ricevuta dal server: " + response);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
