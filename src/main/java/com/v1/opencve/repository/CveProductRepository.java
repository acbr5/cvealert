package com.v1.opencve.repository;

import com.v1.opencve.domainobject.CVEProductDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class CveProductRepository implements ICveProductRepository{
    @Override
    public List<CVEProductDO> findByCveID(Long cveID) {
        return null;
    }

    @Override
    public Optional<CVEProductDO> findByProductID(Long productID) {
        return Optional.empty();
    }

    @Override
    public Optional<CVEProductDO> findByCveName(String cveName) {
        return Optional.empty();
    }

    @Override
    public Optional<CVEProductDO> findByProductName(String productName) {
        return Optional.empty();
    }

    @Override
    public List<CVEProductDO> findByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public List<CVEProductDO> findAll() {
        return null;
    }

    @Override
    public List<CVEProductDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CVEProductDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<CVEProductDO> findAllById(Iterable<Long> longs) {
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
    public void delete(CVEProductDO entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends CVEProductDO> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CVEProductDO> S save(S entity) {
        return null;
    }

    @Override
    public <S extends CVEProductDO> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CVEProductDO> findById(Long aLong) {
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
    public <S extends CVEProductDO> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<CVEProductDO> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CVEProductDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends CVEProductDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CVEProductDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CVEProductDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CVEProductDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CVEProductDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CVEProductDO> boolean exists(Example<S> example) {
        return false;
    }
}
