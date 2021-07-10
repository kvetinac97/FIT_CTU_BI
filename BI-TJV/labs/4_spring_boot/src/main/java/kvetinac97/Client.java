package kvetinac97;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Client {

    private ClientService clientService;

    @Autowired
    public void setClientService ( ClientService clientService ) {
        this.clientService = clientService;
    }

    public void run () {
        while ( true ) {
            String info = clientService.callMethod();
            System.out.println(info);

            try {
                Thread.sleep(5000);
            } catch ( InterruptedException exc ) { exc.printStackTrace(); }
        }
    }

}
