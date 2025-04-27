package com.example.Sprachraume.DailyRoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DailyRoomService {

    private final RestTemplate restTemplate;

    @Value("${daily.api.url}")
    private String dailyApiUrl;

    @Value("${daily.api.key}")
    private String dailyApiKey;

    public String createDailyRoom() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(dailyApiKey);

        Map<String, Object> properties = new HashMap<>();
        properties.put("enable_chat", true);
        properties.put("start_video_off", false);
        properties.put("start_audio_off", false);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("properties", properties);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(dailyApiUrl + "/rooms", request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("url");  // возвращаем ссылку на комнату
        } else {
            throw new RuntimeException("Failed to create Daily room");
        }
    }
}
