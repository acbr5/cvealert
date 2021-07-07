package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.UserDO;
import com.v1.cvealert.service.IUserService;
import com.v1.cvealert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@SessionAttributes
@Controller
public class RegisterAndLoginController {
    @Autowired
    IUserService userService = new UserService();

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String printUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        model.addAttribute("username", name);
        return "user/login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public ModelAndView logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        ModelAndView mv = new ModelAndView("redirect:login");
        return mv;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDO", new UserDO());
        return ("user/register");
    }

    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid UserDO userDO, BindingResult bindingResult,
                                        Model model, RedirectAttributes redirectAttributes,
                                        @RequestParam("username") String username,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        @RequestParam("retype_password") String retype_password,
                                        HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword;
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> params = new HashMap<>();

        UserDO user = new UserDO();

        UserDO userExists = userService.getUserByUsername(userDO.getUsername());
        UserDO userExistsEmail = userService.getUserByEmail(userDO.getEmail());

        GetAvatar.getGravatar(model, 30, userService);

        if (userExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the user name provided");
            redirectAttributes.addFlashAttribute("messages", "Username already exists! Please type another username.");
            params.put("messages1","Username already exists! Please type another username.");
        }
        if(userExistsEmail != null){
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
            redirectAttributes.addFlashAttribute("messages", "Email already exists! Please type another email.");

            params.put("messages2","Email already exists! Please type another email.");
        }
        if (bindingResult.hasErrors()) {
            ModelAndView mvv = new ModelAndView("user/register", params);
            return mvv;
        } else {
            encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            user.setUsername(username);

            userService.createUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new UserDO());
            userService.register(user, getSiteURL(request));
            modelAndView.setViewName("user/register_success");
            return modelAndView;
        }
    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "user/verify_success";
        } else {
            return "user/verify_fail";
        }
    }
}