package ru.prokdo.webtaxifleet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ru.prokdo.webtaxifleet.service.data.DriverService;
import ru.prokdo.webtaxifleet.service.security.UserService;
import ru.prokdo.webtaxifleet.util.PhoneNumberFormatter;

@Controller
public class IndexController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final DriverService driverService;;

    @Autowired
    public IndexController(AuthenticationManager authenticationManager, UserService userService, DriverService driverService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.driverService = driverService;
    }

    @GetMapping("/")
    public String show() {
        return "index";
    }

    @PostMapping("/login")
    public String verifyPhone(
                        @RequestParam("phone") String phone,
                        Model model,
                        HttpServletRequest request) {
        String formattedPhone = PhoneNumberFormatter.format(phone);

        UserDetails user;
        try {
            user = userService.loadUserByUsername(formattedPhone);
        } catch (UsernameNotFoundException exception) {
            exception.printStackTrace();
            model.addAttribute("error", "Пользователь не найден");
            return "index";
        }

        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DRIVER"))) {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(formattedPhone, null, user.getAuthorities())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            session.setAttribute("phone", formattedPhone);
            session.setAttribute("id", driverService.findByPhone(formattedPhone).get().getId());
            session.setAttribute("name", driverService.findByPhone(formattedPhone).get().getFirstName());
            return "redirect:/driver/dashboard";

        } else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"))) {
            model.addAttribute("phone", phone);
            return "administrator/login";
        }

        return "index";
    }
}
