package com.v1.opencve.controller;

import com.v1.opencve.Gravatar;
import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.service.IUserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

// For avatar of user's email
public class GetAvatar {
    public static String getGravatar(Model model, Integer size, IUserService userService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDO userDetails = userService.getUserByUsername(auth.getName());
            String mail = userDetails.getEmail();
            Gravatar.setURL(mail, size);
            String userGravatar = Gravatar.getURL();
            model.addAttribute("gravatar", userGravatar);
            return userGravatar;
        }
        return null;
    }
}