import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class UnlimitedServer {
    private static final int PORT = 777;
    private static LinkedBlockingQueue<String> queue;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server connesso sulla porta " + PORT);

            queue = new LinkedBlockingQueue<>();

            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String clientType = in.readLine();

                if (clientType.equals("producer")) {
                    out.println("okprod");
                    String message = in.readLine();
                    System.out.println("Messaggio ricevuto dal client: " + message);
                    synchronized (lock) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}