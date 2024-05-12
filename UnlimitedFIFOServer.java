import java.io.*;
import java.net.*;
import java.util.*;

public class UnlimitedFIFOServer {
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
                    fifo.add(data);
                } else if (message.equals("consumer")) {
                    out.println("okcons\n");
                    if (fifo.isEmpty()) {
                        out.println("wait");
                        while (fifo.isEmpty()) {
                            Thread.sleep(100);
                        }
                    }
                    String data = fifo.removeFirst();
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