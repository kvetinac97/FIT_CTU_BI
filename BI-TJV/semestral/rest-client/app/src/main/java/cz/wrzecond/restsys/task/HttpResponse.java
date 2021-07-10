package cz.wrzecond.restsys.task;

public final class HttpResponse {

    private final HttpStatus status;
    private final String locationHeader;
    private final String responseBody;

    public HttpResponse (HttpStatus status, String locationHeader, String responseBody) {
        this.status = status;
        this.locationHeader = locationHeader;
        this.responseBody = responseBody;
    }

    public HttpStatus getStatus () {
        return status;
    }

    public String getLocationHeader () {
        return locationHeader;
    }

    public String getResponseBody () {
        return responseBody;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
            "status=" + status +
            ", locationHeader='" + locationHeader + '\'' +
            ", responseBody='" + responseBody + '\'' +
            '}';
    }

}
