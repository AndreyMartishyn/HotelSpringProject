package ua.martishyn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.martishyn.entities.Role;
import ua.martishyn.entities.User;
import ua.martishyn.service.UserService;

import java.util.Map;

@RequestMapping("/user")
@Controller
public class UserController {
    private static final String REDIRECT_USER_MESSAGES = "redirect:/user-messages/";

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String getUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users-list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String editUserForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String saveEditedUser(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        userService.saveUser(username, user, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String showProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email)
    {
        userService.updateUser(user, password, email);
        return "redirect:/user/profile";
    }
    @GetMapping("/subscribe/{user}")
    public String subscribeToUser(@AuthenticationPrincipal User currentUser,
                                  @PathVariable User user) {
        userService.subscribe(currentUser, user);
        return REDIRECT_USER_MESSAGES + user.getId();
    }

    @GetMapping("/unsubscribe/{user}")
    public String unsubscribeFromUser(@AuthenticationPrincipal User currentUser,
                                      @PathVariable User user) {
        userService.unsubscribe(currentUser, user);
        return REDIRECT_USER_MESSAGES + user.getId();
    }


    @GetMapping("{type}/{user}/list")
    public String getUserList(@PathVariable User user,
                              @PathVariable String type,
                              Model model){
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);
        if ("subscriptions".equals(type)){
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }
        return "subscriptions";
    }
}
