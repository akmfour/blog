package com.suzumiya.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suzumiya.blog.entity.Error;

@Repository
public interface ErrRepository extends JpaRepository<Error, Integer>{


}
