package com.example.BankProject.detect.direction.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //고객
    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    //약국
    private String targetBankName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    // 고객 주소와 약국 주소 사의의 거리
    private double distance;
}
