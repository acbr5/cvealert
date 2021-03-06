package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.error.MyAccessDeniedHandler;
import com.v1.opencve.service.CustomUserDetailsService;
import com.v1.opencve.service.IUserService;
import com.v1.opencve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class AppController {
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

    @RequestMapping(value="/vendors", method=RequestMethod.GET)
    public ModelAndView vendors(Model model) {
        ModelAndView mv = new ModelAndView("vendors");
        getGravatar(model, 30);
        return mv;
    }

    @RequestMapping(value="/cwe", method=RequestMethod.GET)
    public ModelAndView cwes(Model model) {
        ModelAndView mv = new ModelAndView("cwes");
        getGravatar(model, 30);
        return mv;
    }

    @RequestMapping(value="/reports", method=RequestMethod.GET)
    public ModelAndView reports(Model model) {
        ModelAndView mv = new ModelAndView("reports");
        getGravatar(model, 30);
        return mv;
    }

    @RequestMapping(value="/admin", method=RequestMethod.GET)
    public ModelAndView listUsers(Model model) {
        getGravatar(model, 30);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDO user = userService.getUserByUsername(auth.getName());
        if(user.getIsAdmin()){
            List<UserDO> listUsers = userService.getAllUsers();
            model.addAttribute("listUsers", listUsers);
            ModelAndView mv = new ModelAndView("admin.html");
            getGravatar(model, 30);
            return mv;
        }
        else {
            MyAccessDeniedHandler ah = new MyAccessDeniedHandler();
            ModelAndView mv = new ModelAndView("/errors/403");
            return mv;
        }
    }

    @RequestMapping(value = "/makeAdmin", method = RequestMethod.POST)
    public String makeAdmin(@ModelAttribute(value="user2") UserDO userDO, Map<String, Object> model) {
        userService.makeAdmin(userDO);
        return "redirect:admin";
    }

    @RequestMapping(value = "/makeUser", method = RequestMethod.POST)
    public String makeUser(@ModelAttribute(value="user3") UserDO userDO, Map<String, Object> model) {
        userService.makeUser(userDO);
        return "redirect:admin";
    }
}
