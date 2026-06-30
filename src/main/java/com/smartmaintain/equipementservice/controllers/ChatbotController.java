package com.smartmaintain.equipementservice.controllers;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin("*")
public class ChatbotController {

    @Value("${gemini.api.key:YOUR_API_KEY_HERE}")
    private String geminiApiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askChatbot(@Valid @RequestBody Map<String, String> request) {
        String role = request.getOrDefault("role", "technician");
        String userMessage = request.getOrDefault("message", "");

        String systemPrompt = getSystemPromptForRole(role);
        String answer = callGemini(systemPrompt, userMessage);
        
        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return ResponseEntity.ok(response);
    }

    private String getSystemPromptForRole(String role) {
        switch (role.toLowerCase()) {
            case "technician":
                return "You are an AI assistant specialized in plane maintenance. Focus on helping the Technician with task execution and safety guidelines.";
            case "engineer":
                return "You are an AI assistant specialized in plane maintenance. Focus on helping the Engineer with task separation, priority, team leadership, and task assignment.";
            case "manager":
                return "You are an AI assistant specialized in plane maintenance. Focus on helping the Manager with fleet overview, cost/efficiency analysis, and the experiences needed for each maintenance.";
            case "admin":
                return "You are an AI assistant specialized in plane maintenance. Focus on helping the Admin with system monitoring, user checking, and overall application health.";
            default:
                return "You are an AI assistant specialized in plane maintenance. Answer queries related to tasks, teams, and rapports.";
        }
    }

    private String callGemini(String systemPrompt, String message) {
        try {
            if (geminiApiKey == null || geminiApiKey.isBlank() || geminiApiKey.equals("YOUR_API_KEY_HERE")) {
                return "Simulated AI Response: GEMINI_API_KEY is not configured.";
            }

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct Gemini Payload
            Map<String, Object> body = new HashMap<>();
            
            // System Instruction
            Map<String, Object> systemInstruction = new HashMap<>();
            Map<String, Object> sysParts = new HashMap<>();
            sysParts.put("text", systemPrompt);
            systemInstruction.put("parts", sysParts);
            body.put("system_instruction", systemInstruction);

            // Contents
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> contentItem = new HashMap<>();
            List<Map<String, Object>> parts = new ArrayList<>();
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", message);
            parts.add(textPart);
            contentItem.put("parts", parts);
            contents.add(contentItem);
            body.put("contents", contents);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> outParts = (List<Map<String, Object>>) content.get("parts");
                    return (String) outParts.get(0).get("text");
                }
            }
            return "I understood your query, but could not formulate a response.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Simulated AI Response (API key might be invalid or network error): Here is a specialized response based on your role. " + e.getMessage();
        }
    }
}
