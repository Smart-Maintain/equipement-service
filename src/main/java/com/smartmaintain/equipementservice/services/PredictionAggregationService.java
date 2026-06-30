package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.PredictionLogRequest;
import com.smartmaintain.equipementservice.entities.PredictionAlert;
import com.smartmaintain.equipementservice.repositories.PredictionAlertRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PredictionAggregationService {

    private final PredictionAlertRepository alertRepository;
    private final NotificationService notificationService;

    // Buffer to hold incoming predictions for 60s window
    private final Map<Long, List<PredictionLogRequest>> enginePredictionBuffer = new ConcurrentHashMap<>();

    // State tracking to achieve "1 copy per recurrence"
    private final Map<Long, String> enginePreviousState = new ConcurrentHashMap<>();

    public PredictionAggregationService(PredictionAlertRepository alertRepository, NotificationService notificationService) {
        this.alertRepository = alertRepository;
        this.notificationService = notificationService;
    }

    public void addPredictionLog(PredictionLogRequest request) {
        if (request.engineId() == null) return;
        enginePredictionBuffer.computeIfAbsent(request.engineId(), k -> new ArrayList<>()).add(request);
    }

    // Runs every 60 seconds
    @Scheduled(fixedRate = 60000)
    public void aggregatePredictions() {
        if (enginePredictionBuffer.isEmpty()) {
            return;
        }

        // Copy and clear the current buffer securely
        Map<Long, List<PredictionLogRequest>> currentBuffer = new ConcurrentHashMap<>(enginePredictionBuffer);
        enginePredictionBuffer.clear();

        for (Map.Entry<Long, List<PredictionLogRequest>> entry : currentBuffer.entrySet()) {
            Long engineId = entry.getKey();
            List<PredictionLogRequest> logs = entry.getValue();
            if (logs.isEmpty()) continue;

            String engineName = logs.get(0).engineName();
            boolean hasAlert = logs.stream().anyMatch(PredictionLogRequest::alert);
            int minRul = logs.stream().mapToInt(PredictionLogRequest::rulCycles).min().orElse(999);

            String currentState = hasAlert ? "CRITICAL" : "NORMAL";
            String previousState = enginePreviousState.getOrDefault(engineId, "NORMAL");

            if ("CRITICAL".equals(currentState) && !"CRITICAL".equals(previousState)) {
                // State changed from normal to critical: Save Alert and Notify!
                PredictionAlert alert = PredictionAlert.builder()
                        .engineId(engineId)
                        .engineName(engineName)
                        .message("ML Prediction Alert on " + engineName + ": Anomaly Detected. Estimated RUL: " + minRul + " cycles.")
                        .type("critical")
                        .timestamp(LocalDateTime.now())
                        .status("PENDING")
                        .build();

                PredictionAlert savedAlert = alertRepository.save(alert);

                // Notify Admins and Managers
                if (notificationService != null) {
                    notificationService.sendNotification(null, "ADMIN", "Critical Alert for Engine " + engineName + ": RUL is " + minRul, "ALERT", null);
                    notificationService.sendNotification(null, "MANAGER", "Critical Alert for Engine " + engineName + ": RUL is " + minRul, "ALERT", null);
                }

                // Update state
                enginePreviousState.put(engineId, "CRITICAL");
            } else if ("NORMAL".equals(currentState) && !"NORMAL".equals(previousState)) {
                // State recovered: Save a Normal log!
                PredictionAlert alert = PredictionAlert.builder()
                        .engineId(engineId)
                        .engineName(engineName)
                        .message("ML Prediction Normal on " + engineName + ": Engine operating normally. Estimated RUL: " + minRul + " cycles.")
                        .type("normal")
                        .timestamp(LocalDateTime.now())
                        .status("PENDING")
                        .build();

                alertRepository.save(alert);

                enginePreviousState.put(engineId, "NORMAL");
            }
        }
    }
}
