package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.ReportsDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class ReportsRepository implements IReportsRepository{

    @Override
    public List<ReportsDO> findByCveID(Long cveID) {
        return null;
    }

    @Override
    public List<ReportsDO> findByUserID(Long userID) {
        return null;
    }

    @Override
    public List<ReportsDO> findAll() {
        return null;
    }

    @Override
    public List<ReportsDO> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ReportsDO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ReportsDO> findAllById(Iterable<Long> longs) {
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
    public void delete(ReportsDO entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends ReportsDO> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends ReportsDO> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ReportsDO> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ReportsDO> findById(Long aLong) {
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
    public <S extends ReportsDO> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<ReportsDO> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ReportsDO getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends ReportsDO> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ReportsDO> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ReportsDO> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ReportsDO> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ReportsDO> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ReportsDO> boolean exists(Example<S> example) {
        return false;
    }
}
