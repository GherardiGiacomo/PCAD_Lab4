import java.io.*;
import java.net.*;
import java.util.*;

public class UnlimitedServer {
    private static LinkedList<String> fifo = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message = in.readLine();

                if (message.equals("producer")) {
                    out.println("okprod\n");
                    String data = in.readLine();
                    synchronized (fifo) {
                        fifo.add(data);
                        fifo.notifyAll(); // Sveglia i consumatori in attesa
                    }
                } else if (message.equals("consumer")) {
                    out.println("okcons\n");
                    String data;
                    synchronized (fifo) {
                        while (fifo.isEmpty()) {
                            fifo.wait(); // Aspetta se la coda è vuota
                        }
                        data = fifo.removeFirst();
                    }
                    out.println(data);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}