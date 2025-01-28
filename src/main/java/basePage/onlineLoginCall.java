package basePage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class onlineLoginCall {

    public JsonNode authResult;
    public static String accessToken;
    public static String idToken;
    public static String refreshToken;
    public static String sub;
    public static String numWithCode;

    public void loginRequest(String username, String password, String endpoint) throws Exception {
        String ENDPOINT;
        String codeNum;

        if (endpoint.contains("internal")) {
            ENDPOINT = "https://internal-backend.dev.aioapp.com";
            codeNum = "+1";
        } else if (endpoint.contains("uatv2")) {
            ENDPOINT = "https://uatv2-backend.dev.aioapp.com";
            codeNum = "+92";
        } else {
            ENDPOINT = "https://backend.dev.aioapp.com";
            codeNum = "+1";
        }
        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Define the request body
        numWithCode = codeNum + username;
        String requestBody = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\"}",
                numWithCode, password);

        // Build the POST request
        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI("https://uatv2-backend.dev.aioapp.com/api/authentication/login-user"))
                .uri(new URI(ENDPOINT + "/api/authentication/login-user"))
                .header("Content-Type", "application/json")
                .header("x-app-name", "online")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print the response body for debugging
//        System.out.println("Response Body: " + response.body());

        // Check for a successful response
        if (response.statusCode() != 200) {
            throw new Exception("Failed to authenticate. HTTP Status: " + response.statusCode() + " Body: " + response.body());
        }

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());

        // Navigate to the required fields
        authResult = rootNode
                .path("data")
                .path("authChallengeResponse")
                .path("AuthenticationResult");

        if (authResult.isMissingNode()) {
            throw new Exception("AuthenticationResult is missing in the response.");
        }


        // Extract the tokens
        accessToken = authResult.has("AccessToken") ? authResult.get("AccessToken").asText() : null;
        idToken = authResult.has("IdToken") ? authResult.get("IdToken").asText() : null;
        refreshToken = authResult.has("RefreshToken") ? authResult.get("RefreshToken").asText() : null;
        // Extract the "sub" field
        JsonNode subNode = rootNode.path("data").path("sub");
        sub = subNode.isMissingNode() ? null : subNode.asText();

        // Handle missing tokens
        if (accessToken == null || idToken == null || refreshToken == null || sub == null) {
            throw new Exception("Missing tokens in the response: " + response.body());
        }
    }
}
