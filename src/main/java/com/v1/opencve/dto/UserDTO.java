package com.v1.opencve.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private String username;

    private String first_name;

    private String last_name;

    private String email;

    private Boolean isAdmin;
}
