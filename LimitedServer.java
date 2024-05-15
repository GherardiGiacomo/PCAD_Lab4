import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class LimitedServer {
    private static final int PORT = 777;
    private static final int MAX_CONNECTIONS = 5; // dimensione max di connessioni num messo a caso
    private static ArrayBlockingQueue<String> queue;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server connesso sulla porta " + PORT);

            queue = new ArrayBlockingQueue<>(MAX_CONNECTIONS);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String clientType = in.readLine();
                // System.out.println("Tipo di client: " + clientType);

                if (clientType.equals("producer")) {
                    out.println("okprod");
                    String message = in.readLine();
                    System.out.println("Messaggio da producer: " + message);
                    synchronized (lock) {
                        while (queue.size() == MAX_CONNECTIONS) {
                            System.out.println("Coda piena, produttore in attesa");
                            lock.wait();
                        }
                        queue.put(message); // se coda piena, si blocca
                        System.out.println("Numero attuale di messaggi nella coda: " + queue.size());
                        lock.notifyAll();
                    }
                } else if (clientType.equals("consumer")) {
                    out.println("okcons");
                    String message = in.readLine();
                    synchronized (lock) {
                        while (queue.isEmpty()) {
                            System.out.println("Coda vuota");
                            System.out.println("Server bloccato");
                            lock.wait();
                        }
                        System.out.println("Messaggio da consumer: " + message);
                        queue.take();
                        System.out.println("Numero attuale di messaggi nella coda: " + queue.size());
                        lock.notifyAll();
                    }
                }

                clientSocket.close();
            }
        } catch (Exception e) {
            // System.err.println("nulla di buono è successo");
            e.printStackTrace();
        }
    }
}