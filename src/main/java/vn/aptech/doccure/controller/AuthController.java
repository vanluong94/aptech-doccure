package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.EmailService;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.utils.StringUtils;
import vn.aptech.doccure.validator.RegisterValidator;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("login")
    String login() {
        return "auth/login";
    }

    @GetMapping("register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("auth/register");
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName(Constants.Roles.ROLE_PATIENT));
        User user = new User(1, roles);
        modelAndView.addObject("user", user);
        modelAndView.addObject("isDoctor", false);
        return modelAndView;
    }

    @GetMapping("doctor-register")
    public ModelAndView doctorRegister() {
        ModelAndView modelAndView = new ModelAndView("auth/register");
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
        User user = new User(1, roles);
        modelAndView.addObject("user", user);
        modelAndView.addObject("isDoctor", true);
        return modelAndView;
    }

    @PostMapping("register")
    public String register(@Validated @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirect) {
        registerValidator.validate(user, result);
        if (result.hasErrors()) {
            return "auth/register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userService.save(user) != null) {
            try {
                emailService.sendMessage(user.getEmail(), "Hello, " + user.getUsername() +"!",
                        "Congratulations, you have successfully registered an account. Please <a href=\"http://localhost:8080/login\">click here</a> to login.\n" +
                        "\n<br/><br/>" +
                        "<strong>[Doccure]</strong>");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            redirect.addFlashAttribute("successMessage", "Register successfully.");
        } else {
            redirect.addFlashAttribute("errorMessage", "Register fail.");
        }
        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam("reset_password_token") String token, Model model, RedirectAttributes redirect) {
        try {
            String decode = new String(Base64Utils.decodeFromString(token));
            long currentTimeMillis = System.currentTimeMillis();
            String[] parts = decode.split("-");
            String username = parts[0];
            long expirationTime = Long.parseLong(parts[1]);
            if (currentTimeMillis <= expirationTime) {
                if (userService.existsByUsername(username)) {
                    model.addAttribute("username", username);
                    return "auth/change-password";
                } else {
                    redirect.addFlashAttribute("errorMessage", "This user could not be found.");
                }
            } else {
                redirect.addFlashAttribute("errorMessage", "Your password reset token has expired.");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "The password reset token is invalid.");
        }
        return "redirect:/forgot-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirect) {
        if (StringUtils.isNullOrBlank(password)) {
            redirect.addFlashAttribute("errorMessage", "Password must not be null or empty!");
        }
        if (StringUtils.isNullOrBlank(confirmPassword)) {
            redirect.addFlashAttribute("errorMessage", "Confirm password must not be null or empty!");
        }
        if (password.equals(confirmPassword)) {
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                user.get().setPassword(passwordEncoder.encode(password));
                user.get().setModifiedDate(LocalDateTime.now());
                user.get().setResetPasswordToken(null);
                if (userService.save(user.get()) != null) {
                    redirect.addFlashAttribute("successMessage", "Awesome, you've successfully updated your password.");
                    return "redirect:/login";
                } else {
                    redirect.addFlashAttribute("errorMessage", "A system error has occurred. Please try again later...");
                }
            } else {
                redirect.addFlashAttribute("errorMessage", "This user could not be found.");
            }
        } else {
            redirect.addFlashAttribute("errorMessage", "The Confirm Password confirmation does not match.");
        }
        return "redirect:/change-password";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, RedirectAttributes redirect) {
        if (StringUtils.isNullOrBlank(email)) {
            redirect.addFlashAttribute("errorMessage", "Email must not be null or empty!");
        }
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            try {
                long expirationTime = System.currentTimeMillis() + Constants.TEN_MINUTES;
                user.get().setResetPasswordToken(expirationTime);
                String encode = Base64Utils.encodeToString((user.get().getUsername() + "-" + expirationTime).getBytes(StandardCharsets.UTF_8));
                if (userService.save(user.get()) != null) {
                    emailService.sendMessage(email, "Reset password instructions",
                            "Hello, " + user.get().getUsername() + "!<br><br>" +
                                    "A request has been received to change the password for your Doccure account.\n" +
                                    "\n<br><br>" +
                                    "<div style=\"text-align: center;\"><a target=\"_blank\" href=\"http://localhost:8080/change-password?reset_password_token=" + encode + "\">Reset My Password</a></div><br><br>" +
                                    "If you did not forgot your password you can safely ignore this email.<br><br>" +
                                    "<strong>[Doccure]</strong>");
                } else {
                    redirect.addFlashAttribute("errorMessage", "A system error has occurred. Please try again later...");
                    return "redirect:/forgot-password";
                }
            } catch (MessagingException e) {
                redirect.addFlashAttribute("errorMessage", "A system error has occurred. Please try again later..." + e.getMessage());
                return "redirect:/forgot-password";
            }
        }
        redirect.addFlashAttribute("successMessage", "If your email address exists in our database, you will receive a password recovery link at your email address in a few minutes.");
        return "redirect:/login";
    }
}
