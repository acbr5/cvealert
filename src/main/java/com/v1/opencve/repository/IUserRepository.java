package com.v1.opencve.repository;

import com.v1.opencve.domainobject.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserDO, Long> {
    Optional<UserDO> findByUsername(String username);

    Optional<UserDO> findByEmail(String username);

    Optional<UserDO> findByResetPasswordToken(String token);

    @Query("SELECT u FROM UserDO u WHERE u.email = ?1")
    public Optional<UserDO> findUserByEmail(String email);

    @Query("SELECT u FROM UserDO u WHERE u.verificationCode = ?1")
    public UserDO findByVerificationCode(String code);
}