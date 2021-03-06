package com.v1.opencve.service;

import com.v1.opencve.domainobject.BlogDO;
import com.v1.opencve.repository.IBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogService implements IBlogService{
    @Autowired
    private IBlogRepository blogRepository;

    @Override
    public BlogDO createBlog(BlogDO blog) {
        return blogRepository.save(blog);
    }

    @Override
    public BlogDO getUserID(Long userID) {
        Optional<BlogDO> currentSub = blogRepository.findByUserID(userID);
        if(currentSub.isPresent()){
            return currentSub.get();
        }
        return null;
    }

    @Override
    public List<BlogDO> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public Boolean isExist(String title) {
        Optional<BlogDO> currentSub = blogRepository.findByTitle(title);
        if(currentSub.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Optional<BlogDO> getBlogByID(Long id) {
        return blogRepository.findById(id);
    }
}
