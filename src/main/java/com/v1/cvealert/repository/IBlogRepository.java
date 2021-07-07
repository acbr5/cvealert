package com.v1.cvealert.repository;

import com.v1.cvealert.domainobject.BlogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<BlogDO, Long> {
    Optional<BlogDO> findByUserID(Long userID);
    Optional<BlogDO> findByTitle(String title);
}
