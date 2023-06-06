package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenAiRequest {
    private String prompt;
    @JsonProperty("max_tokens")
    private int maxTokens;

    // Need to have this ignored so it does not get passed in as part of the request
    @JsonIgnore
    private String model ="text-davinci-003";

    @JsonIgnore
    private String apiEndpoint = "https://api.openai.com/v1/engines/" + model + "/completions";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }
}
