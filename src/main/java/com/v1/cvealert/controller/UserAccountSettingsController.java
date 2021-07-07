package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.*;
import com.v1.cvealert.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserAccountSettingsController {
    @Autowired
    IUserService userService = new UserService();

    @RequestMapping(value="/account/", method= RequestMethod.GET)
    public ModelAndView profile(Model model) {
        ModelAndView mv = new ModelAndView("profiles/base_profile");
        GetAvatar.getGravatar(model, 88, userService);
        return mv;
    }

    @RequestMapping(value="/account/settings", method=RequestMethod.GET)
    public ModelAndView settings(Model model) {
        ModelAndView mv = new ModelAndView("profiles/settings");
        GetAvatar.getGravatar(model, 30, userService);
        return mv;
    }

    @RequestMapping(value="/account/notifications", method=RequestMethod.GET)
    public ModelAndView notifications(Model model) {
        ModelAndView mv = new ModelAndView("profiles/notifications");
        GetAvatar.getGravatar(model, 30, userService);
        return mv;
    }

    @RequestMapping(value = "edit_user_profile", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public ModelAndView edit_user_profile(@Valid UserDO userDO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        GetAvatar.getGravatar(model, 30, userService);

        ModelAndView modelAndView = new ModelAndView();
        UserDO userExists = userService.getUserByUsername(SecurityContextHolder
                .getContext().getAuthentication().getName());

        userExists.setFirst_name(userDO.getFirst_name());
        userExists.setLast_name(userDO.getLast_name());

        if (userExists != null) {
            userService.updateUserWithDTO(userExists);
           // modelAndView.addObject("successMessage", "User has been updated successfully");
            modelAndView.addObject("user", new UserDO());
            model.addAttribute("change_name", "1");
            redirectAttributes.addFlashAttribute("messageErrUpt", "Update User Name is Successful!");
            modelAndView.setViewName("redirect:account/settings");
        }
        else{
            model.addAttribute("change_name", "0");
        }
        return modelAndView;
    }

    @RequestMapping(value="change_password", method=RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')") //only accessible to logged in users
    public ModelAndView change_password(@Valid UserDO userDO, Locale locale, BindingResult bindingResult,
                                           Model model, RedirectAttributes redirectAttributes,
                                           @RequestParam("password") String password,
                                           @RequestParam("oldpassword") String oldPassword) {
        UserDO user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pass = user.getPassword();
        Map<String, Object> params = new HashMap<>();

        if(passwordEncoder.matches(oldPassword, pass)){
            userService.changeUserPassword(user, password);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", new UserDO());
            model.addAttribute("change_password", "1");
            redirectAttributes.addFlashAttribute("msg", "Update User Password is Successful!");
            modelAndView.setViewName("redirect:account/settings");
            return modelAndView;
        }
        else{
            bindingResult
                    .rejectValue("password", "error.user",
                            "Old Password is Wrong!");
            model.addAttribute("change_password", "0");
            redirectAttributes.addFlashAttribute("messageErr", "Old Password is Wrong!");
            params.put("messageErr","old password is wrong!");
            ModelAndView mv = new ModelAndView("redirect:account/settings", params);
            return mv;
        }
    }

    @RequestMapping(value="change_username", method=RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')") //only accessible to logged in users
    public ModelAndView change_username(@Valid UserDO userDO, Locale locale, BindingResult bindingResult,
                                        Model model, RedirectAttributes redirectAttributes,
                                        @RequestParam("password") String password,
                                        @RequestParam("username") String username) {
        UserDO user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pass = user.getPassword();
        Map<String, Object> params = new HashMap<>();

        UserDO userExists = userService.getUserByUsername(userDO.getUsername());
        if(userExists != null){
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the user name provided");
            model.addAttribute("change_username", "0");
            redirectAttributes.addFlashAttribute("errorMsg2", "Username already exists! Please type another username.");
            params.put("errorMsg2","Username already exists! Please type another username.");
            ModelAndView mv = new ModelAndView("redirect:account/settings", params);
            return mv;
        }
        else if(passwordEncoder.matches(password, pass)){
            user.setUsername(username);
            userService.changeUsername(user);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", new UserDO());
            model.addAttribute("change_username", "1");
            redirectAttributes.addFlashAttribute("succMessage", "Update Username is Successful!");
            modelAndView.setViewName("redirect:account/settings");
            return modelAndView;
        }
        else{
            bindingResult
                    .rejectValue("username", "error.user",
                            "Password is Wrong!");
            model.addAttribute("change_username", "0");
            redirectAttributes.addFlashAttribute("errorMsg", "Password is Wrong!");
            params.put("errorMsg","password is wrong!");
            ModelAndView mv = new ModelAndView("redirect:account/settings", params);
            return mv;
        }
    }

    @RequestMapping(value="change_email", method=RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')") //only accessible to logged in users
    public ModelAndView change_email(@Valid UserDO userDO, Locale locale, BindingResult bindingResult,
                                        Model model, RedirectAttributes redirectAttributes,
                                        @RequestParam("password") String password,
                                        @RequestParam("email") String email) {
        UserDO user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pass = user.getPassword();
        Map<String, Object> params = new HashMap<>();

        UserDO userExists = userService.getUserByEmail(userDO.getEmail());
        if(userExists != null){
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
            model.addAttribute("change_email", "0");
            redirectAttributes.addFlashAttribute("errorMsgEmail2", "Email already exists! Please type another email address.");
            params.put("errorMsgEmail2","Email already exists! Please type another email address.");
            ModelAndView mv = new ModelAndView("redirect:account/settings", params);
            return mv;
        }
        else if(passwordEncoder.matches(password, pass)){
            user.setEmail(email);
            userService.changeEmail(user);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", new UserDO());
            model.addAttribute("change_email", "1");
            redirectAttributes.addFlashAttribute("succMessageEmail", "Update Email is Successful!");
            modelAndView.setViewName("redirect:account/settings");
            return modelAndView;
        }
        else{
            bindingResult
                    .rejectValue("email", "error.user",
                            "Password is Wrong!");
            redirectAttributes.addFlashAttribute("errorMsgEmail", "Password is Wrong!");
            model.addAttribute("change_email", "0");
            params.put("errorMsgEmail","password is wrong!");
            ModelAndView mv = new ModelAndView("redirect:account/settings", params);
            return mv;
        }
    }

    @RequestMapping(value="{/account/settings}", method=RequestMethod.GET)
    public ModelAndView tab(@PathVariable String tab, Model model) {
        ModelAndView mv;
        if (Arrays.asList("edit_user_profile", "change_password", "change_username", "change_email")
                .contains(tab)) {
            mv = new ModelAndView("user/"+tab);
            return mv;
        }
        GetAvatar.getGravatar(model, 30, userService);
        return null;
    }
}
