package kvetinac97.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server;

    public boolean setup () {
        try {
            server = new ServerSocket(19132);
            System.out.println("[SERVER] Listening on: " + server.getLocalPort());
            return true;
        }
        catch ( IOException exc ) {
            exc.printStackTrace();
            return false;
        }
    }

    public void start () {
        try {
            while ( true ) {
                Socket socket = server.accept();
                System.out.println("[SERVER] Client joined");
                new Thread(() -> {
                    try { work(socket); } catch (IOException | ClassNotFoundException exc) { exc.printStackTrace(); }
                }).start();
            }
        }
        catch ( IOException exc ) {
            exc.printStackTrace();
        }
    }

    // Samotné posílání dat
    private void work ( Socket socket ) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        String name = (String) ois.readObject();

        System.out.println("Received " + name + ", sending response");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject("Hello, " + name);
        oos.flush();
    }

}
