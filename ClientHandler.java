import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientType = reader.readLine().trim();

            if (clientType.equals("producer")) {
                writer.println("okprod");
                String data = reader.readLine();
                // Aggiungi la logica per gestire la stringa prodotta
            } else if (clientType.equals("consumer")) {
                writer.println("okcons");
                // Invia una stringa al client consumatore
                writer.println("Stringa inviata al client consumatore");
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}