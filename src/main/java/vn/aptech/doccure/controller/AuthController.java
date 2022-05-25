package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        // Kiểm tra token hết hạn thì hiển thị thông báo: Your password reset token has expired.
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
                String token = Base64Utils.encodeToString(email.getBytes(StandardCharsets.UTF_8));
                user.get().setResetPasswordToken(token);
                if (userService.save(user.get()) != null) {
                    emailService.sendMessage(email, "Reset password instructions",
                            "Hello, " + user.get().getFullName() + "!<br><br>" +
                                    "A request has been received to change the password for your Doccure account.\n" +
                                    "\n<br><br>" +
                                    "<div style=\"text-align: center;\"><a target=\"_blank\" href=\"http://localhost:8080/forgot-password?reset_password_token=" + token + "\">Reset My Password</a></div><br><br>" +
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
