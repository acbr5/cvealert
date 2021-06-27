package com.v1.opencve.controller;

import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.service.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    IUserService userService = new UserService();

    String email;

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public ModelAndView forgotPassword(Model model) {
        ModelAndView mv = new ModelAndView("user/forgot_password");
        GetAvatar.getGravatar(model, 30, userService);
        return mv;
    }

    @PostMapping("/forgot_password")
    public ModelAndView processForgotPass(@Valid UserDO userDO, BindingResult bindingResult,
                                          Model model, RedirectAttributes redirectAttributes,
                                          HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> params = new HashMap<>();

        UserDO userExistsEmail = userService.getUserByEmail(userDO.getEmail());

        if(userExistsEmail == null){
            bindingResult
                    .rejectValue("email", "error.user",
                            "There isn't a user registered with the email provided");
            redirectAttributes.addFlashAttribute("messages", "This e-mail does not exist in the records. Please type another email.");

            params.put("error","This e-mail does not exist in our records. Please type another email.");
        }
        if (bindingResult.hasErrors()) {
            ModelAndView mvv = new ModelAndView("/user/forgot_password", params);
            return mvv;
        } else {
            modelAndView.addObject("successMessage", "The password has been resetted successfully");

            email = request.getParameter("email");
            String token = RandomString.make(30);

            try {
                userService.updateResetPasswordToken(token, email);
                String resetPasswordLink = getSiteURL(request) + "/reset_password?token=" + token;
                sendEmail(email, resetPasswordLink);
                model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

            } catch (UserNotFoundException ex) {
                model.addAttribute("error", ex.getMessage());
            } catch (UnsupportedEncodingException | MessagingException e) {
                model.addAttribute("error", "Error while sending email");
            }

            modelAndView.setViewName("user/forgot_password");
            return modelAndView;
        }
    }

    public void sendEmail(String email, String ResetPasswordToken) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("cvealert.v2@gmail.com", "Aisha from CVEAlert.com");
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + ResetPasswordToken + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.GET)
    public String resetPassword(@Param(value = "token") String token, Model model) {
        if(token == null || userService.getByResetPasswordToken(token) == null){
            return("errors/emptyToken");
        }

        UserDO userDO = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (userDO == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        GetAvatar.getGravatar(model, 30, userService);
        return "user/reset_password";
    }

    @PostMapping("/reset_password")
    public String processResetPass(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        if(token == null || userService.getByResetPasswordToken(token) == null){
            return("errors/emptyToken");
        }

        UserDO userDO = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (userDO == null) {
            model.addAttribute("error", "Invalid Token");
        } else {
            userService.updatePassword(userDO, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "user/reset_password";
    }

    @RequestMapping(value = "/errors/emptyToken", method = RequestMethod.GET)
    public String emptyToken(Model model, Principal principal) {

        GetAvatar.getGravatar(model, 30, userService);

        if (principal != null) {
            UserDO loginedUser = (UserDO) ((Authentication) principal).getPrincipal();

            String userInfo = loginedUser.toString();

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> Empty Token or Used Before ";
            model.addAttribute("message", message);
        }
        return "errors/emptyToken";
    }

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}