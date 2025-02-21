package me.mmtr.springbootrolebasedauth.controller;

import me.mmtr.springbootrolebasedauth.data.User;
import me.mmtr.springbootrolebasedauth.data.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import me.mmtr.springbootrolebasedauth.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationService authenticationService,
                                    UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Incorrect username or password provided");
        }
        if (logout != null) {
            model.addAttribute("logout", logout);
        }
        return "login";
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername()).orElse(null);

        if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
            bindingResult.rejectValue("username", "exists", "This username is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "register";
        }

        authenticationService.registerUser(userDTO, RoleName.USER);
        return "redirect:/login";
    }
}
