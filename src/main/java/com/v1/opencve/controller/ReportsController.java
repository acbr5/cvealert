package com.v1.opencve.controller;

import com.v1.opencve.service.IUserService;
import com.v1.opencve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportsController {
    @Autowired
    IUserService userService = new UserService();

    @RequestMapping(value="/reports", method= RequestMethod.GET)
    public ModelAndView reports(Model model) {
        ModelAndView mv = new ModelAndView("reports");
        GetAvatar.getGravatar(model, 30, userService);
        return mv;
    }
}
