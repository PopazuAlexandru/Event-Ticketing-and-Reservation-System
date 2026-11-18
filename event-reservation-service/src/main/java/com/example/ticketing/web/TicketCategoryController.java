package com.example.ticketing.web;

import com.example.ticketing.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lists pricing categories accepted by the reservation API.
 */
@RestController
@RequestMapping("/api/ticket-categories")
public class TicketCategoryController {
    private final ReservationService reservationService;

    public TicketCategoryController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<String> listCategories() {
        return reservationService.getTicketCategories();
    }
}
