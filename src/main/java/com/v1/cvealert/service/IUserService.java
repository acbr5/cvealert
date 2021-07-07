package com.v1.cvealert.service;

import com.v1.cvealert.domainobject.UserDO;
import com.v1.cvealert.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Transactional
public interface IUserService{
    UserDO createUser(UserDO user);

    UserDO getUserByUsername(String username);

    UserDO getUserByEmail(String email);

    UserDO getUserByUserID(Long userID);

    Long getIDByUsername(String username);

    Boolean checkIfValidOldPassword(String username, String password);

    UserDTO changeUserPassword(UserDO user, String password);

    UserDTO changeUsername(UserDO user);

    UserDTO changeEmail(UserDO user);

    List<UserDO> getAllUsers();

    UserDTO makeAdmin(UserDO userDO);

    UserDTO makeUser(UserDO userDO);

    UserDTO updateUserWithDTO(UserDO user);

    void deleteUser(Long userID);

    void register(UserDO user, String siteURL) throws UnsupportedEncodingException, MessagingException;

    void sendVerificationEmail(UserDO user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);

    void updateResetPasswordToken(String token, String email);

    UserDO getByResetPasswordToken(String token);

    void updatePassword(UserDO userDO, String newPassword);
}
