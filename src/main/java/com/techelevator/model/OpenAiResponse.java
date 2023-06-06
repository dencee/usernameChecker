package com.techelevator.model;

import com.fasterxml.jackson.databind.JsonNode;

public class OpenAiResponse {

    public boolean checkNode(JsonNode node){
        if(!node.isArray() || node.size() == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean checkUsername(JsonNode node) {
        JsonNode topNode = node.get(0);
        String topText = topNode.get("text").asText().trim();
        System.out.println(topText);

        return topText.equals("Yes");
    }
}
