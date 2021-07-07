package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.UserDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository{

    @Override
    public Optional<UserDO> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDO> findByEmail(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDO> findByResetPasswordToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDO> findUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDO findByVerificationCode(String code) {
        return null;
    }


    @Override
    public List<UserDO> findAll() {
        return null;
    }

    @Override
    public List<UserDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<UserDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserDO> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(UserDO userDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends UserDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends UserDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<UserDO> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<UserDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends UserDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends UserDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserDO> boolean exists(Example<S> example) {
        return false;
    }
}
