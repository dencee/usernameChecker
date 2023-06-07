package com.techelevator.dao;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class OpenAiConfiguration {

    private HttpHeaders headers = new HttpHeaders();

    private String model ="text-davinci-003";

    private String apiEndpoint = "https://api.openai.com/v1/engines/" + model + "/completions";

    public OpenAiConfiguration(String apiKey) {

        // Create headers, set type to JSON, add api key as bearer auth key
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Get api key from my local environment variables to keep prying eyes off my key
        headers.setBearerAuth(apiKey);
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
}