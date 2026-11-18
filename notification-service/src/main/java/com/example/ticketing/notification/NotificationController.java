package com.example.ticketing.notification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only API for inspecting simulated notification deliveries.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationLogService notificationLogService;

    public NotificationController(NotificationLogService notificationLogService) {
        this.notificationLogService = notificationLogService;
    }

    @GetMapping
    public List<NotificationLog> getNotifications() {
        return notificationLogService.getLogs();
    }
}
