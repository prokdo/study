package ru.prokdo.webtaxifleet.controller.administrator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.prokdo.webtaxifleet.dto.DriverDTO;
import ru.prokdo.webtaxifleet.service.data.CarService;
import ru.prokdo.webtaxifleet.service.data.DriverService;
import ru.prokdo.webtaxifleet.service.data.TripService;
import ru.prokdo.webtaxifleet.util.PhoneNumberFormatter;

@Controller
public class AdministratorDriverController {
    private final DriverService driverService;
    private final CarService carService;
    private final TripService tripService;

    @Autowired
    public AdministratorDriverController(DriverService driverService, CarService carService, TripService tripService) {
        this.driverService = driverService;
        this.carService = carService;
        this.tripService = tripService;
    }

    @GetMapping("/administrator/drivers/add")
    public String show(Model model) {
        model.addAttribute("driver", new DriverDTO());
        return "administrator/drivers/add";
    }

    @GetMapping("/administrator/drivers/delete")
    public String show() {
        return "administrator/drivers/delete";
    }

    @GetMapping("/administrator/drivers/view")
    public String show(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "licenseNumber", required = false) String licenseNumber,
            @RequestParam(value = "isFree", required = false) Boolean isFree,
            Model model) {
        List<DriverDTO> drivers = driverService.findAll(licenseNumber, isFree, page, limit);
        model.addAttribute("drivers", drivers);

        return "administrator/drivers/view";
    }

    @PostMapping("/administrator/drivers/add")
    public String add(@ModelAttribute("driver") DriverDTO driverDTO, Model model) {
        try {
            if (Period.between(driverDTO.getBirthDate(), LocalDate.now()).getYears() < 18) {
                model.addAttribute("errorMessage", "Возраст водителя должен быть не меньше 18 лет");
                return "administrator/drivers/add";
            }

            if (driverService.existsByLicenseNumber(driverDTO.getLicenseNumber())) {
                model.addAttribute("errorMessage", "Водитель с таким номером водительского удостоверения уже зарегистрирован");
                return "administrator/drivers/add";
            }

            if (driverService.existsByPhone(driverDTO.getPhone())) {
                model.addAttribute("errorMessage", "Водитель с таким номером телефона уже зарегистрирован");
                return "administrator/drivers/add";
            }

            driverDTO.setPhone(PhoneNumberFormatter.format(driverDTO.getPhone()));
            driverDTO.setHireDate(LocalDate.now());

            driverService.add(driverDTO);
            model.addAttribute("successMessage", "Водитель добавлен успешно!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при добавлении водителя: " + e.getMessage());
        }
        return "administrator/drivers/add";
    }

    @PostMapping("/administrator/drivers/delete")
    public String delete(@RequestParam("id") String id, Model model) {
        try {
            if (carService.findByDriverId(Integer.parseInt(id)).isPresent() ||
                !tripService.findAll(Integer.parseInt(id), null, true, null, 0, 1).isEmpty()) {
                model.addAttribute("errorMessage", "Невозможно удалить водителя, у которого есть автомобиль или активный рейсах");
                return "administrator/drivers/delete";
            }
            driverService.deleteById(Integer.parseInt(id));
            model.addAttribute("successMessage", "Водитель с ID " + id + " успешно удалён");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении водителя: " + e.getMessage());
        }
        return "administrator/cars/delete";
    }
}
