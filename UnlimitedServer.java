/* import java.io.BufferedReader;
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
} */

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
                String message;
                if (clientType.equals("producer")) {
                    out.println("okprod");
                    message = in.readLine();
                    synchronized (lock) {
                        queue.put(message);
                        System.out.println("Messaggio ricevuto: " + message);
                        System.out.println("Numero attuale di messaggi nella coda: " + queue.size());
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
                        //String message = queue.take();
                        message = in.readLine();
                        System.out.println("Messaggio da consumer: " + message);
                        System.out.println("Numero attuale di messaggi nella coda: " + queue.size());
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