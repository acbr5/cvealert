package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MyErrorController implements ErrorController {
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

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorPage = "error"; // default

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        getGravatar(model, 30);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                errorPage = "errors/404";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 403 Forbidden error
                errorPage = "errors/403";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                errorPage = "errors/500";

            }
        }
        return errorPage;
    }

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(value = "/errors/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        getGravatar(model, 30);

        if (principal != null) {
            UserDO loginedUser = (UserDO) ((Authentication) principal).getPrincipal();

            String userInfo = loginedUser.toString();

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
        }
        return "errors/403";
    }
}