package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.service.CustomUserDetailsService;
import com.v1.opencve.service.IUserService;
import com.v1.opencve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@SessionAttributes
@Controller
public class LoginController {
    @Autowired
    IUserService userService = new UserService();

    @Autowired
    CustomUserDetailsService userDetailsService;

    // For user image of user's email
    private String getGravatar(Model model, Integer size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)){
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(auth.getName());
            String mail = userDetails.getEmail();
            Gravatar.setURL(mail, size);
            String userGravatar = Gravatar.getURL();
            model.addAttribute("gravatar", userGravatar);
            return userGravatar;
        }
        return null;
    }

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
                                        @RequestParam("retype_password") String retype_password) throws UnsupportedEncodingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword;
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> params = new HashMap<>();

        UserDO user = new UserDO();

        UserDO userExists = userService.getUserByUsername(userDO.getUsername());
        UserDO userExistsEmail = userService.getUserByEmail(userDO.getEmail());

        getGravatar(model, 30);

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
            modelAndView.setViewName("user/register_success");
            return modelAndView;
        }
    }
}