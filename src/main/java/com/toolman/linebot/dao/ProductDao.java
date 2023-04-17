package com.toolman.linebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolman.linebot.entity.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

    public Product findByName(String name);
    
}
