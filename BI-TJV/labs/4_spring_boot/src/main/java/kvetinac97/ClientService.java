package kvetinac97;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

    private final RestTemplate restTemplate;

    public ClientService() {
        this.restTemplate = new RestTemplate();
    }

    public String callMethod () {
        String url = "http://localhost:8080/";
        return restTemplate.getForObject(url, String.class);
    }

}
