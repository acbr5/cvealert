package com.v1.cvealert.controller;

import com.v1.cvealert.domainobject.ReportsDO;
import com.v1.cvealert.domainobject.UserDO;
import com.v1.cvealert.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Controller
public class ReportsController {
    @Autowired
    IUserService userService = new UserService();

    @Autowired
    IReportsService reportsService = new ReportsService();

    @Autowired
    ICVEService cveService = new CVEService();

    @RequestMapping("/reports")
    public String reports(Model model) throws IOException, ParseException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDO currentUser = userService.getUserByUsername(username);

        List<ReportsDO> reports = reportsService.getReportsByUserID(currentUser.getId());;

        model.addAttribute("listReports", reports);
        model.addAttribute("size", reports.size());

        GetAvatar.getGravatar(model, 30, userService);
        return "reports";
    }
}
