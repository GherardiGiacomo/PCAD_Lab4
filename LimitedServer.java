import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LimitedServer {
    private static final int PORT = 777;
    private static final int MAX_CONNECTIONS = 5; // dimensione max di connessioni
    private static ArrayBlockingQueue<String> queue;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server connesso sulla porta " + PORT);

            queue = new ArrayBlockingQueue<>(MAX_CONNECTIONS);
            System.out.println("Il numero massimo di connessioni è settato a: "+ MAX_CONNECTIONS);

            ExecutorService executor = Executors.newFixedThreadPool(MAX_CONNECTIONS);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                executor.submit(() -> {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        String clientType = in.readLine();

                        if (clientType.equals("producer")) {
                            out.println("okprod");
                            String message = in.readLine();
                            System.out.println("Messaggio ricevuto dal client: " + message);
                            synchronized (lock) {
                                while (queue.size() == MAX_CONNECTIONS) {
                                    System.out.println("Coda piena, produttore in attesa");
                                    lock.wait();
                                }
                                queue.put(message);
                                lock.notifyAll();
                            }
                        } else if (clientType.equals("consumer")) {
                            out.println("okcons");
                            synchronized (lock) {
                                while (queue.isEmpty()) {
                                    System.out.println("Coda vuota");
                                    System.out.println("Server bloccato");
                                    lock.wait();
                                }
                                queue.take();
                                String message = in.readLine();
                                System.out.println("Messaggio ricevuto dal client: " + message);
                                lock.notifyAll();
                            }
                        }

                        clientSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}