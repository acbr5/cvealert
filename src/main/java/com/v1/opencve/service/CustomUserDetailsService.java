package com.v1.opencve.service;

import com.v1.opencve.component.CustomUserDetails;
import com.v1.opencve.domainobject.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService = new UserService();

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserDO user = userService.getUserByUsername(usernameOrEmail);
        if (user == null) {
            UserDO userDO = userService.getUserByEmail(usernameOrEmail);
            if(userDO == null){
                throw new UsernameNotFoundException("User not found");
            }
            else return new CustomUserDetails(userDO);
        }
        else return new CustomUserDetails(user);
    }
}