package com.techelevator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.OpenAiRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class OpenAIMain2 {

    public static void main(String[] args) {

        // Get api key from my local environment variables to keep prying eyes off my key
        String apiKey = "";
        apiKey = System.getenv("openAiApiKey");

        RestTemplate restTemplate = new RestTemplate();

        if (apiKey == null) {
            System.out.println("This requires you to set a Windows environment variable named openAiApiKey.");
            System.out.println("You can set this in Windows by typing Environment Variables from the Start Menu,");
            System.out.println("clicking Environment Variables, and clicking New.");
            System.out.println("The key can be acquired through OpenAI.");
        } else {
            System.out.print("Please enter a username to check: ");

            Scanner input = new Scanner(System.in);

            String username = input.nextLine();

            // Had to be very explicit to try to get a yes or no answer here.
            String prompt = "Could the username '" + username + "' be considered offensive, or does it contain any offensive words? Answer only yes or no.";

            // Limit processing power!
            int maxTokens = 100;

            OpenAiRequest postData = new OpenAiRequest();

            // Stable version from OpenAI as I created this; this may be updated in the future
            String model = "text-davinci-003";
            //String model = postData.getModel();

            // Generate endpoint URL
            String apiEndpoint = "https://api.openai.com/v1/engines/" + model + "/completions";

            // Create headers, set type to JSON, add api key as bearer auth key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // OpenAiRequest is the model for the request - in this case we only send the prompt and max_tokens
            postData.setPrompt(prompt);
            postData.setMaxTokens(maxTokens);

            // Finalize request with the post data and the headers
            HttpEntity<OpenAiRequest> entity = new HttpEntity<>(postData, headers);

            // Post to the OpenAI endpoint, get a String result
            try{

                JsonNode result2 = restTemplate.postForObject(postData.getApiEndpoint(), entity, JsonNode.class);
                try {
                    JsonNode choicesNode = result2.get("choices");
                    if (choicesNode.isArray() && choicesNode.size() > 0) {
                        JsonNode firstChoiceNode = choicesNode.get(0);
                        String firstChoiceText = firstChoiceNode.get("text").asText().trim();
                        System.out.println(firstChoiceText);
                        if (firstChoiceText.equals("Yes")) {
                            System.out.println("The username " + username + " is NOT okay.");
                        } else {
                            System.out.println("The username " + username + " is okay.");
                        }
                    } else {
                        System.out.println("No choices found in the JSON response.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }catch (RestClientResponseException e){
                System.out.println("RestClientResponseException");
                System.out.println("Error code: " + e.getRawStatusCode() + " " + e.getStatusText());
            }catch (ResourceAccessException e){
                System.out.println("ResourceAccessException");
                System.out.println(e.getMessage());
            }
        }
    }
}