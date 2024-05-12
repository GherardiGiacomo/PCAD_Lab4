import java.io.PrintWriter;
import java.net.Socket;

public class ProducerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 777;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("producer");
            writer.println("Stringa da inviare al server");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
