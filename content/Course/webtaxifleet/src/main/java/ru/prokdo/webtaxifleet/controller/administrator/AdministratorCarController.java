package ru.prokdo.webtaxifleet.controller.administrator;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.prokdo.webtaxifleet.dto.CarDTO;
import ru.prokdo.webtaxifleet.service.data.CarService;

@Controller
public class AdministratorCarController {
    private final CarService carService;

    @Autowired
    public AdministratorCarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/administrator/cars/add")
    public String show(Model model) {
        model.addAttribute("car", new CarDTO());
        return "administrator/cars/add";
    }

    @GetMapping("/administrator/cars/delete")
    public String show() {
        return "administrator/cars/delete";
    }

    @GetMapping("/administrator/cars/view")
    public String show(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "licensePlate", required = false) String licensePlate,
            @RequestParam(value = "isFree", required = false) Boolean isFree,
            Model model) {
                List<CarDTO> cars = carService.findAll(licensePlate, isFree, page, limit);
                model.addAttribute("cars", cars);

                return "administrator/cars/view";
    }

    @PostMapping("/administrator/cars/add")
    public String add(@ModelAttribute("car") CarDTO carDTO, Model model) {
        try {
            if (carDTO.getRegistrationDate().isAfter(LocalDate.now())) {
                model.addAttribute("errorMessage", "Дата регистрации автомобиля не может быть позже текущей");
                return "administrator/cars/add";
            }

            if (carService.existsByLicensePlate(carDTO.getLicensePlate())) {
                model.addAttribute("errorMessage", "Автомобиль с таким регистрационным номером уже существует");
                return "administrator/cars/add";
            }

            carService.add(carDTO);
            model.addAttribute("successMessage", "Автомобиль добавлен успешно!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при добавлении автомобиля: " + e.getMessage());
        }
        return "administrator/cars/add";
    }

    @PostMapping("/administrator/cars/delete")
    public String delete(@RequestParam("id") String id, Model model) {
        try {
            if (carService.findById(Integer.parseInt(id)).get().getDriverId() != null) {
                model.addAttribute("errorMessage", "Автомобиль не может быть удалён, поскольку им владеет водитель");
                return "administrator/cars/delete";
            }
            carService.deleteById(Integer.parseInt(id));
            model.addAttribute("successMessage", "Автомобиль с ID " + id + " успешно удалён");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении автомобиля: " + e.getMessage());
        }
        return "administrator/cars/delete";
    }
}
