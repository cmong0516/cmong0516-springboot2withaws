package com.mong.book.springboot.web.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts,Long> {
    @Query("select p from Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
