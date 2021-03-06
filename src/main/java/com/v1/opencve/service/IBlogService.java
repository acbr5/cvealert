package com.v1.opencve.service;

import com.v1.opencve.domainobject.BlogDO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface IBlogService {
    BlogDO createBlog(BlogDO blog);
    BlogDO getUserID(Long userID);
    List<BlogDO> getAllBlogs();
    Boolean isExist(String title);
    Optional<BlogDO> getBlogByID(Long id);
}
