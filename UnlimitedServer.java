import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;

public class UnlimitedServer {
    private static ArrayList<String> fifo = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(777)) {
            System.out.println("Server connesso alla porta 777.\nIn attesa di un messaggio da un client:");
            while (true) {
                new Thread(new ClientHandler(serverSocket.accept())).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    if (data != null) {
                        System.out.println("Dati ricevuti dal client: " + data);
                        synchronized (fifo) {
                            fifo.add(data);
                            fifo.notifyAll();
                        }
                    } else {
                        System.out.println("Nessun dato ricevuto dal client.");
                    }
                } else if (message.equals("consumer")) {
                    out.println("okcons\n");
                    String data = in.readLine();
                    if (data != null) {
                        System.out.println("Dati ricevuti dal client: " + data);
                        synchronized (fifo) {
                            while (fifo.isEmpty()) {
                                try {
                                    fifo.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            data = fifo.remove(0);
                            out.println(data);
                        }
                    } else {
                        System.out.println("Nessun dato ricevuto dal client.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}