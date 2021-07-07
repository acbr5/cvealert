package com.v1.cvealert.component;

import com.v1.cvealert.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private IUserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("email");
        System.out.println("onAuthenticationFailure email: " + email);
        request.setAttribute("email", email);

        String redirectURL = "/login?error&email=" + email;

        if (exception.getMessage().contains("OTP")) {
            redirectURL = "/login?otp=true&email=" + email;
        }

        super.setDefaultFailureUrl(redirectURL);
        super.onAuthenticationFailure(request, response, exception);
    }
}