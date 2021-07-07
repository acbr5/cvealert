package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.VendorDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class VendorRepository implements IVendorRepository{
    @Override
    public Optional<VendorDO> findByVendorName(String vendor_name) {
        return Optional.empty();
    }

    @Override
    public List<VendorDO> findAll() {
        return null;
    }

    @Override
    public List<VendorDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<VendorDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<VendorDO> findAllById(Iterable<Long> iterable) {
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
    public void delete(VendorDO vendorDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends VendorDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends VendorDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends VendorDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<VendorDO> findById(Long aLong) {
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
    public <S extends VendorDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<VendorDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public VendorDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends VendorDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends VendorDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends VendorDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends VendorDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends VendorDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends VendorDO> boolean exists(Example<S> example) {
        return false;
    }
}
