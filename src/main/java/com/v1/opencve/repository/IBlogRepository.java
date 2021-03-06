package com.v1.opencve.repository;

import com.v1.opencve.domainobject.BlogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<BlogDO, Long> {
    Optional<BlogDO> findByUserID(Long userID);
    Optional<BlogDO> findByTitle(String title);
}
