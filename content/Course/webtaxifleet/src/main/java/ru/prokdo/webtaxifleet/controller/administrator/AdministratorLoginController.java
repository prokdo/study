package ru.prokdo.webtaxifleet.controller.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ru.prokdo.webtaxifleet.security.HashPasswordEncoder;
import ru.prokdo.webtaxifleet.service.data.AdministratorService;
import ru.prokdo.webtaxifleet.service.security.UserService;
import ru.prokdo.webtaxifleet.util.PhoneNumberFormatter;

@Controller
public class AdministratorLoginController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final HashPasswordEncoder passwordEncoder;
    private final AdministratorService administratorService;

    @Autowired
    public AdministratorLoginController(AuthenticationManager authenticationManager, UserService userService, HashPasswordEncoder passwordEncoder, AdministratorService administratorService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.administratorService = administratorService;
    }

    @GetMapping("/administrator/login")
    public String show(@RequestParam(value = "phone") String phone, Model model) {
        if (phone != null) {
            model.addAttribute("phone", phone);
        }
        return "administrator/login";
    }

    @PostMapping("/administrator/login")
    public String verifyPassword(
                        @RequestParam("phone") String phone,
                        @RequestParam("password") String password,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {
        String formattedPhone = PhoneNumberFormatter.format(phone);

        try {
            UserDetails user = userService.loadUserByUsername(formattedPhone);
            if (passwordEncoder.matches(password, user.getPassword())) {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(formattedPhone, password, user.getAuthorities())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                session.setAttribute("id", administratorService.findByPhone(formattedPhone).get().getId());
                session.setAttribute("phone", formattedPhone);
                session.setAttribute("name", administratorService.findByPhone(formattedPhone).get().getFirstName());
                return "redirect:/administrator/dashboard";
            } else {
                model.addAttribute("phone", phone);
                model.addAttribute("error", "Указан неверный пароль");
                return "administrator/login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("phone", phone);
            model.addAttribute("error", "Указан неверный пароль");
            return "administrator/login";
        }
    }
}
