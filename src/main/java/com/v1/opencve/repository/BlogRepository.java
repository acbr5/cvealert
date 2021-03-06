package com.v1.opencve.repository;

import com.v1.opencve.domainobject.BlogDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class BlogRepository implements IBlogRepository{

    @Override
    public Optional<BlogDO> findByUserID(Long userID) {
        return Optional.empty();
    }

    @Override
    public Optional<BlogDO> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public List<BlogDO> findAll() {
        return null;
    }

    @Override
    public List<BlogDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<BlogDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<BlogDO> findAllById(Iterable<Long> iterable) {
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
    public void delete(BlogDO subscriptionsDO) {

    }

    @Override
    public void deleteAll(Iterable<? extends BlogDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends BlogDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends BlogDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<BlogDO> findById(Long aLong) {
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
    public <S extends BlogDO> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<BlogDO> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public BlogDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends BlogDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends BlogDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends BlogDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends BlogDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends BlogDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends BlogDO> boolean exists(Example<S> example) {
        return false;
    }
}
