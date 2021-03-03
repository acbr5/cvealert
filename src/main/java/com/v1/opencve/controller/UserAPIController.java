package com.v1.opencve.controller;


import com.v1.opencve.domainobject.UserDO;
import com.v1.opencve.dto.UserDTO;
import com.v1.opencve.service.IUserService;
import com.v1.opencve.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserAPIController {

    @Autowired
    private IUserService userService = new UserService();

    @ApiOperation(value = "Create A New User")
    @PostMapping(path = "/users", consumes = "application/json", produces = "application/json")
    ResponseEntity<UserDTO> createUser(@RequestBody UserDO userDO){
        UserDO createdUser = userService.createUser(userDO);
        return new ResponseEntity(createdUser, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get User by Username")
    @GetMapping(path = "/users/{username}")
    ResponseEntity<UserDO> getUserByUsername(@PathVariable String username){
        UserDO user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Updates An Existing User")
    @PutMapping(path = "/users", consumes = "application/json", produces = "application/json")
    ResponseEntity<UserDTO> updateUserWithDTO(@RequestBody UserDO userDO){
        UserDTO updatedUserWithDTO = userService.updateUserWithDTO(userDO);
        return new ResponseEntity(updatedUserWithDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get All Users")
    @GetMapping(path = "/users")
    ResponseEntity<List<UserDO>> getAllUsers(){
        List<UserDO> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete User By UserID")
    @DeleteMapping("/users/{userID}")
    ResponseEntity<String> deleteUser(@PathVariable Long userID){
        userService.deleteUser(userID);
        return new ResponseEntity<>("User with id: "+userID + " is DELETED.", HttpStatus.OK);
    }
}
