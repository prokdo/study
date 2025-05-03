package ru.prokdo.webtaxifleet.controller.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdministratorDashboardController {
    @GetMapping("/administrator/dashboard")
    public String dashboard(HttpSession session, Model model) {
        model.addAttribute("name", session.getAttribute("name"));
        return "administrator/dashboard";
    }
}
