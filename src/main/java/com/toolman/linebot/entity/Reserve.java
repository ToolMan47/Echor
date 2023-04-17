package com.toolman.linebot.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "RESERVE")
@Data
@NoArgsConstructor
@ToString
public class Reserve {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   
    @Column
    private String userId;
    @Column
    private String userName;
    @Column(columnDefinition = "DATE")
    private LocalDate reserveDate;
    

}
