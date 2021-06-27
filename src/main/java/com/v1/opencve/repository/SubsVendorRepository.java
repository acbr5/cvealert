package com.v1.opencve.repository;

import com.v1.opencve.domainobject.SubsVendorDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class SubsVendorRepository implements ISubsVendorRepository{
    @Override
    public Optional<SubsVendorDO> findByVendorID(Long vendorID) {
        return Optional.empty();
    }

    @Override
    public Optional<SubsVendorDO> findByUserID(Long userID) {
        return Optional.empty();
    }

    @Override
    public List<SubsVendorDO> findAll() {
        return null;
    }

    @Override
    public List<SubsVendorDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<SubsVendorDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<SubsVendorDO> findAllById(Iterable<Long> iterable) {
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
    public void delete(SubsVendorDO subscriptionsDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends SubsVendorDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends SubsVendorDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends SubsVendorDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<SubsVendorDO> findById(Long aLong) {
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
    public <S extends SubsVendorDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<SubsVendorDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public SubsVendorDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends SubsVendorDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends SubsVendorDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends SubsVendorDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends SubsVendorDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends SubsVendorDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends SubsVendorDO> boolean exists(Example<S> example) {
        return false;
    }
}
