package com.v1.opencve.service;

import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.dto.UserDTO;
import com.v1.opencve.repository.IUserRepository;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserDO createUser(UserDO user) {
        user.setEmail_confirmed_at(new Date());
        user.setEnable_notifications(true);
        user.setFrequency_notifications("always");
        user.setIsActive(false);
        user.setIsAdmin(false);
        return userRepository.save(user);
    }

    @Override
    public UserDO getUserByUsername(String username) {
        Optional<UserDO> currentUser = userRepository.findByUsername(username);
        if(currentUser.isPresent()){
            return currentUser.get();
        }
        return null;
    }

    @Override
    public UserDO getUserByEmail(String email) {
        Optional<UserDO> currentUser = userRepository.findByEmail(email);
        if(currentUser.isPresent()){
            return currentUser.get();
        }
        return null;
    }

    @Override
    public Long getIDByUsername(String username) {
        Optional<UserDO> currentUser = userRepository.findByUsername(username);
        if(currentUser.isPresent()){
            return currentUser.get().getId();
        }
        return null;
    }

    @Override
    public Boolean checkIfValidOldPassword(String username, String password) {
        Optional<UserDO> currentUser = userRepository.findByUsername(username);
        String encodedPassword;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(currentUser.isPresent()){
            encodedPassword = passwordEncoder.encode(password);
             if(currentUser.get().getPassword() == encodedPassword){
                 return true;
             }
             else return false;
        }
        return null;
    }

    @Override
    public UserDTO changeUserPassword(UserDO user, String password) {
        long userID = user.getId();
        Optional<UserDO> currentUser = userRepository.findById(userID);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword;

        if(currentUser.isPresent()) {
            encodedPassword = passwordEncoder.encode(password);
            currentUser.get().setPassword(encodedPassword);
            userRepository.save(currentUser.get());

            UserDTO userDTO = new ModelMapper().map(currentUser.get(), UserDTO.class);

            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO changeUsername(UserDO user) {
        long userID = user.getId();
        Optional<UserDO> currentUser = userRepository.findById(userID);

        if(currentUser.isPresent()) {
            currentUser.get().setUsername(user.getUsername());
            userRepository.save(currentUser.get());

            UserDTO userDTO = new ModelMapper().map(currentUser.get(), UserDTO.class);

            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO changeEmail(UserDO user) {
        long userID = user.getId();
        Optional<UserDO> currentUser = userRepository.findById(userID);

        if(currentUser.isPresent()) {
            currentUser.get().setEmail(user.getEmail());
            userRepository.save(currentUser.get());

            UserDTO userDTO = new ModelMapper().map(currentUser.get(), UserDTO.class);

            return userDTO;
        }
        return null;
    }

    @Override
    public List<UserDO> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO makeAdmin(UserDO userDO) {
        long userID = userDO.getId();
        Optional<UserDO> currentUser = userRepository.findById(userID);
        if(currentUser.isPresent()){
            currentUser.get().setIsAdmin(true);
            userRepository.save(currentUser.get());
            UserDTO userDTO = new ModelMapper().map(currentUser.get(), UserDTO.class);
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO makeUser(UserDO userDO) {
        long userID = userDO.getId();
        Optional<UserDO> currentUser = userRepository.findById(userID);
        if(currentUser.isPresent()){
            currentUser.get().setIsAdmin(false);
            userRepository.save(currentUser.get());
            UserDTO userDTO = new ModelMapper().map(currentUser.get(), UserDTO.class);
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO updateUserWithDTO(UserDO user) {
        long userID = user.getId();
        Optional<UserDO> currentUser = userRepository.findById(userID);
        if(currentUser.isPresent()){
            currentUser.get().setFirst_name(user.getFirst_name());
            currentUser.get().setLast_name(user.getLast_name());

            userRepository.save(currentUser.get());

            UserDTO userDTO = new ModelMapper().map(currentUser.get(), UserDTO.class);

            return userDTO;
        }
        return null;
    }

    @Override
    public void deleteUser(Long userID) {
        Optional<UserDO> currentCustomer = userRepository.findById(userID);
        if(currentCustomer.isPresent()){
            userRepository.deleteById(userID);
        }
    }

    @Override
    public void register(UserDO user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setIsActive(false);

        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }

    @Override
    public void sendVerificationEmail(UserDO user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "cvealert.v2@gmail.com";
        String senderName = "Aisha from CVEAlert.com";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">CLICK HERE TO VERIFYING YOUR REGISTRATION</a></h3>"
                + "OR COPY THE LINK: <br>"
                + "[[URL]]<br>"
                + "<br>Thank you,<br>"
                + "CVEAlert.com.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public boolean verify(String verificationCode) {
        UserDO user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.getIsActive()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setIsActive(true);
            userRepository.save(user);

            return true;
        }
    }

    // Reset Password
    @Override
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        Optional<UserDO> userDO = userRepository.findByEmail(email);
        if (userDO != null) {
            userDO.get().setResetPasswordToken(token);
            userRepository.save(userDO.get());
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    @Override
    public UserDO getByResetPasswordToken(String token) {
        if(userRepository.findByResetPasswordToken(token).isEmpty()){
            return null;
        }
        else return userRepository.findByResetPasswordToken(token).get();
    }

    @Override
    public void updatePassword(UserDO userDO, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        userDO.setPassword(encodedPassword);

        userDO.setResetPasswordToken(null);
        userRepository.save(userDO);
    }
}
