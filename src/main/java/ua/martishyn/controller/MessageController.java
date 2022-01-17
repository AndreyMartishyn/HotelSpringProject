package ua.martishyn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.martishyn.entities.Message;
import ua.martishyn.entities.User;
import ua.martishyn.service.MessageService;
import ua.martishyn.service.UserService;
import ua.martishyn.service.ValidationUtils;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    @Value("${upload.path}")
    private String uploadPath;
    private static final String MESSAGE_ATTRIBUTE = "message";
    private static final String MESSAGES_ATTRIBUTE = "messages";
    private static final String REDIRECT_USER_MESSAGES = "redirect:/user-messages/";


    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @PostMapping("/main")
    public String addMessage(@AuthenticationPrincipal User user,
                             @Valid Message message,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("file") MultipartFile file) {

        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ValidationUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute(MESSAGE_ATTRIBUTE, message);
            model.addAttribute("tag", message.getTag());
        } else {
            addFileToMessage(message, file);
            model.addAttribute(MESSAGE_ATTRIBUTE, null);
            messageService.save(message);
        }
        Iterable<Message> messages = messageService.findAll();
        model.addAttribute(MESSAGES_ATTRIBUTE, messages);
        return "main";
    }

    private void addFileToMessage(Message message, MultipartFile file) {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + '/' + resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            message.setFilename(resultFileName);
        }
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageService.findByTag(filter);
        } else {
            messages = messageService.findAll();
        }
        model.put(MESSAGES_ATTRIBUTE, messages);
        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {

        Set<Message> messages = user.getMessages();
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute(MESSAGES_ATTRIBUTE, messages);
        model.addAttribute(MESSAGE_ATTRIBUTE, message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "user-messages";
    }

    @PostMapping("/user-messages/{user}/edit")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long user,
                                @RequestParam Message message,
                                @RequestParam("text") String text,
                                @RequestParam("tag") String tag,
                                @RequestParam("file") MultipartFile image) {
        if (message.getAuthor().equals(currentUser)) {
            if (StringUtils.isEmpty(text) && StringUtils.isEmpty(tag)) {
                return REDIRECT_USER_MESSAGES + user;
            } else {
                message.setText(text);
                message.setTag(tag);
                addFileToMessage(message, image);
                messageService.save(message);
            }
        }
        return REDIRECT_USER_MESSAGES + user;
    }

    @GetMapping("/user-messages/{user}/delete")
    public String deleteMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestParam(name = "message") Long id) {
        if (currentUser.equals(user)) {
            messageService.deleteMessageById(id);
        }
        return REDIRECT_USER_MESSAGES + user.getId();
    }
}
