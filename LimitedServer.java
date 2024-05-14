import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class LimitedServer {
    private static final int PORT = 777;
    private static final int MAX_CONNECTIONS = 10; //dimensione max di connessioni 10 messo a caso
    private static ArrayBlockingQueue<String> queue;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server avviato sulla porta " + PORT);

            queue = new ArrayBlockingQueue<>(MAX_CONNECTIONS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connessione stabilita con " + clientSocket);

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String clientType = in.readLine();

                if (clientType.equals("producer")) {
                    out.println("okprod");
                    String message = in.readLine();
                    queue.put(message); //se coda piena, si blocca
                    System.out.println("Messaggio ricevuto: " + message);
                } else if (clientType.equals("consumer")) {
                    out.println("okcons");
                    String message = queue.take(); //se coda vuota, si blocca
                    out.println(message);
                    System.out.println("Messaggio inviato: " + message);
                }

                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}