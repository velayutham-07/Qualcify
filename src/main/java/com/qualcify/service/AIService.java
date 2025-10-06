package com.qualcify.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AIService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AIService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.generativeai.google/v1") // Replace with actual Gemini REST endpoint
                .defaultHeader("Authorization", "Bearer " + System.getenv("GEMINI_API_KEY"))
                .build();
    }

    /**
     * Sends a prompt to Gemini API and returns the AI response.
     * @param prompt The input prompt to send
     * @return AI response as a String
     */
    public String getPrediction(String prompt) {
        try {
            Mono<Map> response = webClient.post()
                    .uri("/models/text-bison-001:generate") // Replace with actual endpoint
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "prompt", Map.of("text", prompt),
                            "temperature", 0.5,
                            "maxOutputTokens", 500
                    ))
                    .retrieve()
                    .bodyToMono(Map.class);

            Map result = response.block(); // blocking call
            if (result != null && result.containsKey("candidates")) {
                var candidates = (java.util.List<Map<String, Object>>) result.get("candidates");
                if (!candidates.isEmpty() && candidates.get(0).containsKey("content")) {
                    return candidates.get(0).get("content").toString();
                }
            }
            return "No prediction returned from AI.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to get prediction: " + e.getMessage();
        }
    }
}