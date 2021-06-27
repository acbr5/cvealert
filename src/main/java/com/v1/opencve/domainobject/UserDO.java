package com.v1.opencve.domainobject;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data

@Entity
@Table(name = "users")
public class UserDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "verification_code", nullable = true, length = 64)
    private String verificationCode="";

    @Column(nullable = false)
    private Boolean isActive=false;

    public UserDO(){
        super();
    }

    // User authentication information
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, length = 254)
    private String password;
    @Column(name = "reset_password_token", nullable = true, length = 100)
    private String resetPasswordToken="";

    // User email information
    @Column(nullable = false, length = 254, unique = true)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date email_confirmed_at;

    @PrePersist
    private void onCreate() {
        email_confirmed_at = new Date();
    }

    //Notification parameters
    @Column(nullable = false)
    private Boolean enable_notifications = true;
    private String filters_notifications = "";
    private String frequency_notifications = "always";

    // User information
    @Column(nullable = true, length = 100)
    private String first_name="";
    @Column(nullable = true, length = 100)
    private String last_name="";
    @Column(nullable = false)
    private Boolean isAdmin = false;
}