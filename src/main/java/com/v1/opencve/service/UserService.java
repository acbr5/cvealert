package com.v1.opencve.service;

import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.dto.UserDTO;
import com.v1.opencve.repository.IUserRepository;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    @Override
    public UserDO createUser(UserDO user) {
        user.setEmail_confirmed_at(new Date());
        user.setEnable_notifications(true);
        user.setFrequency_notifications("always");
        user.setIsActive(true);
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

    public void register(UserDO user, String siteURL)
            throws UnsupportedEncodingException{
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setIsActive(false);

        userRepository.save(user);
    }
}
