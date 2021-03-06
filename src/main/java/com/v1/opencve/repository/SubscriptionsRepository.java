package com.v1.opencve.repository;

import com.v1.opencve.domainobject.SubscriptionsDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class SubscriptionsRepository implements ISubscriptionsRepository{
    @Override
    public Optional<SubscriptionsDO> findByVendorID(Long vendorID) {
        return Optional.empty();
    }

    @Override
    public Optional<SubscriptionsDO> findByUserID(Long userID) {
        return Optional.empty();
    }

    @Override
    public List<SubscriptionsDO> findAll() {
        return null;
    }

    @Override
    public List<SubscriptionsDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<SubscriptionsDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<SubscriptionsDO> findAllById(Iterable<Long> iterable) {
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
    public void delete(SubscriptionsDO subscriptionsDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends SubscriptionsDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends SubscriptionsDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends SubscriptionsDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<SubscriptionsDO> findById(Long aLong) {
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
    public <S extends SubscriptionsDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<SubscriptionsDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public SubscriptionsDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends SubscriptionsDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends SubscriptionsDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends SubscriptionsDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends SubscriptionsDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends SubscriptionsDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends SubscriptionsDO> boolean exists(Example<S> example) {
        return false;
    }
}
