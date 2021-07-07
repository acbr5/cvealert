package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.CVEDO;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

public class CVERepository implements ICVERepository {
    @Override
    public Optional<CVEDO> findByCveid(String cve_id) {
        return Optional.empty();
    }


    @Override
    public Iterable<CVEDO> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public Optional<CVEDO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<CVEDO> findAll(){
        return  null;
    }

    @Override
    public Page<CVEDO> findByOrderByIdDesc(Pageable pageable) {
        return null;
    }

    @Override
    public Page<CVEDO> findAll(Pageable pageable) {
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
    public void delete(CVEDO cvedo) {

    }

    @Override
    public void deleteAll(Iterable<? extends CVEDO> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CVEDO> S save(S s) {
        return null;
    }

    @Override
    public <S extends CVEDO> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }
}