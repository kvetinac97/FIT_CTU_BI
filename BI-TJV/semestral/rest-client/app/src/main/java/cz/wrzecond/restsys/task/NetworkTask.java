package cz.wrzecond.restsys.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class NetworkTask extends AsyncTask<Void, Void, HttpResponse> {

    // === PARAMETERS ===
    private Consumer<HttpResponse> consumer;
    private HttpRequest request;

    public NetworkTask (Consumer<HttpResponse> consumer, HttpRequest request) {
        this.consumer = consumer;
        this.request = request;
        this.executeOnExecutor(THREAD_POOL_EXECUTOR);
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            // Init request
            Log.d("RestSys", request.getMethod() + " " + request.getUrl());
            if (request.getBody() != null)
                Log.d("RestSys", "Body: " + request.getBody());

            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod(request.getMethod().name());

            // Init request headers
            request.getHeaders().entrySet().forEach(entry ->
                connection.setRequestProperty(entry.getKey(), entry.getValue())
            );

            // Init request body
            if (request.getBody() != null && !request.getBody().isEmpty()) {
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", "application/json");
                OutputStream os = connection.getOutputStream();
                os.write(request.getBody().getBytes());
                os.flush();
            }

            String output = null;
            if (connection.getResponseCode() == HttpStatus.OK.getCode()) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                StringBuilder sb = new StringBuilder();
                while ( (output = br.readLine()) != null )
                    sb.append(output);
                output = sb.toString();
            }

            // Return response
            HttpResponse response = new HttpResponse(
                HttpStatus.fromCode(connection.getResponseCode()),
                connection.getHeaderFields().containsKey("Location") ? connection.getHeaderField("Location") : null,
                output
            );

            connection.disconnect();
            Log.d("RestSys", response.toString());
            return response;
        }
        catch (IOException|RuntimeException x) {
            x.printStackTrace();
            return new HttpResponse(HttpStatus.UNAVAILABLE, null, null);
        }
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        super.onPostExecute(result);
        consumer.accept(result);
    }

}
