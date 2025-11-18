package sim.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sim.config.BackendConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class BackendClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String deviceEndp;

    public BackendClient(BackendConfig config){
        httpClient= HttpClient.newHttpClient();
        this.baseUrl= "http://localhost:8081";
        this.deviceEndp = "/api/devices";


    }
    public List<DeviceDTO> fetchDevices(){
        List<DeviceDTO> deviceList;
        String deviceUrl = baseUrl+deviceEndp;
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(deviceUrl))
                    .GET()
                    .build();
           response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid device endpoint URL: " + deviceUrl, e);
        }
        catch (IOException e) {
            throw new IllegalStateException("Cannot reach backend at " + deviceUrl, e);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // required best practice
            throw new IllegalStateException("HTTP request to backend interrupted", e);
        }
        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Unexpected HTTP status " + response.statusCode() +
                            " while calling " + deviceUrl +
                            " Body: " + response.body()
            );
        }
        String body = response.body();
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("Backend returned empty device list: " + deviceUrl);
        }

        List<DeviceDTO> result;
        try {
            result = mapper.readValue(body, new TypeReference<List<DeviceDTO>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JSON response from backend: " + deviceUrl, e);
        }

        return result;


    }
}
