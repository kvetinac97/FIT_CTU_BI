package cz.wrzecond.restsys.task;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final String SERVER_URL = "http://dev.justtalk.cz:8080";

    // === PROPERTIES ===
    private final String url;
    private final HttpMethod method;
    private Map<String, String> headers;
    private String body = null;

    public HttpRequest (String url, HttpMethod method) {
        this.url = SERVER_URL + url;
        this.headers = new HashMap<>();
        this.method = method;
        if (method == HttpMethod.GET)
            this.body = null;
    }

    public String getUrl() {
        return url;
    }
    public HttpMethod getMethod () {
        return method;
    }
    public Map<String, String> getHeaders () {
        return headers;
    }
    public String getBody () {
        return body;
    }

    public void addHeader (String name, String value) {
        headers.put(name, value);
    }
    public void setBody(String body) {
        if (method == HttpMethod.GET)
            throw new UnsupportedOperationException("GET method cannot have body!");
        this.body = body;
    }

}
