package com.toolman.linebot.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toolman.linebot.entity.Reserve;

public interface ReserveDao extends JpaRepository<Reserve, Integer> {
    
    public Reserve findByUserId(String userId);

    public Reserve findByReserveDate(LocalDate reserveDate);

    public Reserve findByUserIdAndReserveDate(String userId, LocalDate reserveDate);
    
    public List<Reserve> findByReserveDateBetween(LocalDate start, LocalDate end);
    
    public List<Reserve> findByUserIdAndReserveDateBetween(String userId, LocalDate start, LocalDate end);
    
    public Reserve deleteByUserIdAndReserveDate(String userId, LocalDate reserveDate);
}
