package ru.prokdo.webtaxifleet.controller.administrator;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.prokdo.webtaxifleet.dto.TripDTO;
import ru.prokdo.webtaxifleet.service.data.TripService;

@Controller
public class AdministratorTripController {
    private final TripService tripService;

    @Autowired
    public AdministratorTripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/administrator/trips/view")
    public String show(
            @RequestParam(value = "driverId", required = false) Integer driverId,
            @RequestParam(value = "carId", required = false) Integer carId,
            @RequestParam(value = "inProgress", required = false) Boolean inProgress,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            Model model) {
        List <TripDTO> trips = tripService.findAll(driverId, carId, inProgress, endDate, page, limit);
        model.addAttribute("trips", trips);
        return "administrator/trips/view";
    }
}
