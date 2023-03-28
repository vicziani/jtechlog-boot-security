package jtechlog.jtechlogbootsecurity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping(value = "/")
    public ModelAndView index(@AuthenticationPrincipal User user) {
        log.debug("Logged in user: {}", user);
        return new ModelAndView("index", Map.of("users", userService.listUsers(), "user", new User()));
    }

    @PostMapping(value = "/")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/";
    }
}
