package com.example.ticketing.web;

import com.example.ticketing.domain.event.Event;
import com.example.ticketing.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST endpoints for event discovery and creation.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {
    private final ReservationService reservationService;

    public EventController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Event> listEvents() {
        return reservationService.getEvents();
    }

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable UUID eventId) {
        return reservationService.getEvent(eventId);
    }

    @PostMapping
    public Event createEvent(@RequestBody CreateEventRequest request) {
        return reservationService.createEvent(
                request.title(), request.location(), request.dateTime(), request.totalCapacity());
    }
}
