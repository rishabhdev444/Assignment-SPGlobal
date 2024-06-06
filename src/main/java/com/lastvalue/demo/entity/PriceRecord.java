package com.lastvalue.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
//@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PriceRecord {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private LocalDateTime asOf;
    private double payLoad;
}
