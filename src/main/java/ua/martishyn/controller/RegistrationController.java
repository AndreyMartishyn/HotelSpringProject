package ua.martishyn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ua.martishyn.entities.User;
import ua.martishyn.entities.CaptchaResponseDto;
import ua.martishyn.service.UserService;
import ua.martishyn.service.ValidationUtils;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final String REGISTER_PAGE = "registration";
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Autowired
    private UserService userService;
    @Value("${recaptcha.secret}")
    private String secret;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String register() {
        return REGISTER_PAGE;
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("confirmPassword") String confirmPassword,
                          @RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse );
        CaptchaResponseDto response  =  restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (!response.isSuccess()){
            model.addAttribute("captchaError", "Captcha not solved :)");
        }
        boolean isConfirmedPassword = StringUtils.isEmpty(confirmPassword);
        if (isConfirmedPassword){
            model.addAttribute("passwordConfirmError", "Password confirmation cannot be empty") ;
        }
        if (user.getPassword()!=null && !user.getPassword().equals(confirmPassword)){
            model.addAttribute("passwordError", "Passwords mismatch");
        }
        if (!response.isSuccess() ||  isConfirmedPassword || bindingResult.hasErrors()){
            Map<String, String> errors = ValidationUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return REGISTER_PAGE;
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists");
            return REGISTER_PAGE;
        }
        userService.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/activation/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User is activated!");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "User code not found!");
        }
        return "login";
    }
}
