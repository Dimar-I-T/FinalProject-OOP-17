package com.dimar.floorislavabackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {
    @GetMapping("/health")
    Map<String, Object> healthCheck() {
        Map<String, Object> map = new HashMap<>();
        map.put("Status", "UP");
        map.put("message", "Jetpack Joyride Backend is running");
        map.put("timestamp", System.currentTimeMillis());
        return map;
    }

    @GetMapping("/info")
    Map<String, Object> info() {
        Map<String, Object> utama = new HashMap<>();
        utama.put("nama", "CS6_Dimar_Backend");
        utama.put("version", "1.0");
        utama.put("description", "aplikasi mantap");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("players", "/api/players");
        endpoints.put("scores", "/api/scores");
        utama.put("endpoints", endpoints);
        return utama;
    }
}

