package com.toolman.linebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolman.linebot.entity.Store;

public interface StoreDao extends JpaRepository<Store, Integer> {

    public Store findByName(String name);
    
}
