package ru.prokdo.webtaxifleet.controller.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import ru.prokdo.webtaxifleet.dto.CarDTO;
import ru.prokdo.webtaxifleet.dto.TripDTO;
import ru.prokdo.webtaxifleet.service.data.CarService;
import ru.prokdo.webtaxifleet.service.data.DriverService;
import ru.prokdo.webtaxifleet.service.data.TripService;

@Controller
public class DriverDashboardController {
    private final TripService tripService;
    private final CarService carService;
    private final DriverService driverService;

    @Autowired
    public DriverDashboardController(TripService tripService, CarService carService, DriverService driverService) {
        this.tripService = tripService;
        this.carService = carService;
        this.driverService = driverService;
    }

    @GetMapping("/driver/dashboard")
    public String show(
            @RequestParam(value = "freeCar", required = false) boolean freeCar,
            @RequestParam(value = "endTrip", required = false) boolean endTrip,
            HttpSession session,
            Model model) {
        model.addAttribute("name", session.getAttribute("name"));

        CarDTO carDTO = carService.findByDriverId((Integer) session.getAttribute("id")).orElse(null);
        if (freeCar) {
            carService.freeCar(carDTO.getId());
            return "redirect:/driver/dashboard";
        }
        model.addAttribute("currentCar", carDTO);

        TripDTO tripDTO = null;
        List<TripDTO> tripDTOList = null;
        if  (carDTO != null) {
            tripDTOList = tripService.findAll(carDTO.getDriverId(), carDTO.getId(), true, null, 0, 1);
            tripDTO = tripDTOList.isEmpty()? null : tripDTOList.get(0);
        }
        model.addAttribute("currentTrip", tripDTO);

        if (endTrip) {
            tripService.endTrip(tripDTO.getId());
            driverService.freeDriver((Integer) session.getAttribute("id"));

            return "redirect:/driver/dashboard";
        }

        return "driver/dashboard";
    }
}
