package com.v1.opencve.repository;

import com.v1.opencve.domainobject.SubsProductDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class SubsProductRepository implements ISubsProductRepository{
    @Override
    public Optional<SubsProductDO> findByProductID(Long productID) {
        return Optional.empty();
    }

    @Override
    public Optional<SubsProductDO> findByUserID(Long userID) {
        return Optional.empty();
    }

    @Override
    public List<SubsProductDO> findAll() {
        return null;
    }

    @Override
    public List<SubsProductDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<SubsProductDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<SubsProductDO> findAllById(Iterable<Long> iterable) {
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
    public void delete(SubsProductDO subscriptionsDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends SubsProductDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends SubsProductDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends SubsProductDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<SubsProductDO> findById(Long aLong) {
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
    public <S extends SubsProductDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<SubsProductDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public SubsProductDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends SubsProductDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends SubsProductDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends SubsProductDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends SubsProductDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends SubsProductDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends SubsProductDO> boolean exists(Example<S> example) {
        return false;
    }
}
