package kvetinac97;

import kvetinac97.network.Client;
import kvetinac97.network.Server;

public class Network {

    public static void main ( String[] args ) {

        Server server = new Server();

        if ( !server.setup() )
            return;

        new java.lang.Thread(server::start).start();

        Client client = new Client();

        System.out.println("Sending client request");
        client.sendRequest();
        client.sendRequest();
        client.sendRequest();

    }

}
