package cinema_management.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cinema_management.repository.UserRepository;
import cinema_management.entities.User;
import cinema_management.helper.Message;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    // Home Page
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Online Movie Ticket Booking System");
        return "home";
    }

    // Registration Page
    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Registration - Online Movie Ticket Booking System");
        model.addAttribute("user", new User() );
        return "signup";
    }
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               Model model, HttpSession session ) {

        try {
            if(!agreement) {
                throw new Exception(" You have not agreed the terms and condition.");
            }
            user.setRole("ROLE_USER");
            user.setEnable(true);
            user.setImgUrl("User Profile Default Photo.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            this.userRepository.save(user);
            model.addAttribute("user", new User() );
            session.setAttribute("message", new Message("Successfully Registered.", "alert-success"));
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message", new Message("Something went wrong." + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }


    @GetMapping("/login")
    public String customLogin(Model model) {
        model.addAttribute("title", "Login");
        return "login";

    }
    @GetMapping("/change_password")
    private String getChangePassword(){
        return "change_password";
    }
    @PostMapping("/change_password")
    public String processChangePassword(@RequestParam("currentPassword") String currentPassword,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("confirmPassword") String confirmPassword,
                                        Principal principal, Model model, HttpSession httpSession) {
        User currentUser =  this.userRepository.getUserByUserName(principal.getName());
        boolean isPasswordMatch = passwordEncoder.matches(currentPassword, currentUser.getPassword());
        if (!isPasswordMatch) {
            model.addAttribute("error", true);
            httpSession.setAttribute("message", new Message("Mật khẩu cũ sai ! ", "danger"));
            return "change_password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error_confirm", true);
            httpSession.setAttribute("message", new Message("Mật khẩu nhập lại sai! ", "danger"));
            return "change_password";
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(currentUser);
        httpSession.setAttribute("message", new Message("Đổi mật khẩu thành công", "success"));
        model.addAttribute("success", true);
        return"/login";
    }
}

