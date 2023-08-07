package com.mysite.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.shop.entity.Test;

public interface TestRepository extends JpaRepository<Test,Long>{

}
