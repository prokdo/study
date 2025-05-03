package ru.prokdo.webtaxifleet.controller.driver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import ru.prokdo.webtaxifleet.dto.TripDTO;
import ru.prokdo.webtaxifleet.service.data.CarService;
import ru.prokdo.webtaxifleet.service.data.DriverService;
import ru.prokdo.webtaxifleet.service.data.TripService;

@Controller
public class DriverTripController {
    private final TripService tripService;
    private final CarService carService;
    private final DriverService driverService;

    @Autowired
    public DriverTripController(TripService tripService, CarService carService, DriverService driverService) {
        this.tripService = tripService;
        this.carService = carService;
        this.driverService = driverService;
    }

    @GetMapping("/driver/trips/add")
    public String show(
            @RequestParam(value = "startLocation", required = false) String startLocation,
            @RequestParam(value = "endLocation", required = false) String endLocation,
            HttpSession session,
            Model model) {
        if (startLocation != null) {
            TripDTO tripDTO = new TripDTO(
                null,
                (Integer) session.getAttribute("id"),
                carService.findByDriverId((Integer) session.getAttribute("id")).get().getId(),
                LocalDateTime.now(),
                null,
                startLocation,
                endLocation);
            tripService.add(tripDTO);

            driverService.occupyDriver((Integer) session.getAttribute("id"));

            return "redirect:/driver/dashboard";
        }

        return "driver/trips/add";
    }

    @GetMapping("/driver/trips/view")
    public String view(
            @RequestParam(value = "carId", required = false) Integer carId,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            HttpSession session,
            Model model) {
        List <TripDTO> trips = tripService.findAll((Integer) session.getAttribute("id"), carId, false, endDate, page, limit);
        model.addAttribute("trips", trips);
        return "driver/trips/view";
    }
}
