package ru.prokdo.webtaxifleet.controller.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import ru.prokdo.webtaxifleet.dto.CarDTO;
import ru.prokdo.webtaxifleet.service.data.CarService;

@Controller
public class DriverCarController {
    private final CarService carService;

    @Autowired
    public DriverCarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/driver/cars")
    public String show(
            @RequestParam(value = "carId", required = false) Integer carId,
            HttpSession session,
            Model model) {
        List<CarDTO> cars = carService.findAll(null, true, 0, 100);
        model.addAttribute("cars", cars);

        if (carId != null) {
            if (carService.findById(carId).isEmpty()) {
                model.addAttribute("errorMessage", "Машины с указанным идентификатором не существует");
                return "driver/cars";
            }

            Integer driverId = (Integer) session.getAttribute("id");
            if (carService.occupyCar(carId, driverId)) {
                return "redirect:/driver/dashboard";
            } else {
                model.addAttribute("errorMessage", "Машина с указанным идентификатором уже занята другим водителем. Обновите страницу, чтобы увидеть актуальный список машин");
                return "driver/cars";
            }
        }

        return "driver/cars";
    }
}
