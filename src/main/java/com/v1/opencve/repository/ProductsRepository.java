package com.v1.opencve.repository;

import com.v1.opencve.domainobject.ProductsDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class ProductsRepository implements IProductsRepository{
    @Override
    public Optional<ProductsDO> findByVendorID(Long vendorID) {
        return Optional.empty();
    }

    @Override
    public Optional<ProductsDO> findByProductName(String productName) {
        return Optional.empty();
    }

    @Override
    public List<ProductsDO> findAll() {
        return null;
    }

    @Override
    public List<ProductsDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ProductsDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ProductsDO> findAllById(Iterable<Long> iterable) {
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
    public void delete(ProductsDO productsDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends ProductsDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends ProductsDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends ProductsDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<ProductsDO> findById(Long aLong) {
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
    public <S extends ProductsDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<ProductsDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ProductsDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends ProductsDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ProductsDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ProductsDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ProductsDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ProductsDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ProductsDO> boolean exists(Example<S> example) {
        return false;
    }
}
