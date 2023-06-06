package com.techelevator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenAIMain {

    public static void main(String[] args) {

        // Get api key from my local environment variables to keep prying eyes off my key
        String apiKey = "";
        apiKey = System.getenv("openAiApiKey");

        if(apiKey == null){
            System.out.println("This requires you to set a Windows environment variable named openAiApiKey.");
            System.out.println("You can set this in Windows by typing Environment Variables from the Start Menu,");
            System.out.println("clicking Environment Variables, and clicking New.");
            System.out.println("The key can be acquired through OpenAI.");
        }
        else {
            try {
                System.out.print("Please enter a username to check: ");

                Scanner input = new Scanner(System.in);

                String username = input.nextLine();

                // Had to be very explicit to try to get a yes or no answer here.
                String prompt = "Could the username '" + username + "' be considered offensive, or does it contain any offensive words? Answer only yes or no.";

                // Limit processing power!
                int maxTokens = 100;

                // Stable version from OpenAI as I created this; this may be updated in the future
                String model = "text-davinci-003";

                String apiEndpoint = "https://api.openai.com/v1/engines/" + model + "/completions";

                // Set up connection to API
                URL url = new URL(apiEndpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);

                String postData = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": " + maxTokens + "}";
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                System.out.println(postData);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response.toString());

                        JsonNode choicesNode = jsonNode.get("choices");
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
                } else {
                    System.out.println("API request failed with response code: " + connection.getResponseCode());
                    System.out.println(connection.toString());
                }
                connection.disconnect();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
}