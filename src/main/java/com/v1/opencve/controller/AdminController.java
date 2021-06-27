package com.v1.opencve.controller;

import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.error.MyAccessDeniedHandler;
import com.v1.opencve.service.IUserService;
import com.v1.opencve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {
    @Autowired
    IUserService userService = new UserService();

    @RequestMapping(value="/admin", method=RequestMethod.GET)
    public ModelAndView listUsers(Model model) {
        GetAvatar.getGravatar(model, 30, userService);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDO user = userService.getUserByUsername(auth.getName());
        if(user.getIsAdmin()){
            List<UserDO> listUsers = userService.getAllUsers();
            model.addAttribute("listUsers", listUsers);
            ModelAndView mv = new ModelAndView("admin.html");
            GetAvatar.getGravatar(model, 30, userService);
            return mv;
        }
        else {
            MyAccessDeniedHandler ah = new MyAccessDeniedHandler();
            ModelAndView mv = new ModelAndView("errors/403");
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
