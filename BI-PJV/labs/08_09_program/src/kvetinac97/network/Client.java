package kvetinac97.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    public void sendRequest () {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 19132);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("Karel");
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String response = (String) ois.readObject();
                System.out.println("Got server response: " + response);
                socket.close();
            }
            catch ( IOException | ClassNotFoundException exc ) {
                exc.printStackTrace();
            }
        }).start();
    }

}
