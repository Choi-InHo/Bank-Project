package com.example.BankProject.detect.bank.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDto {

    private Long id;

    private String bankName;
    private String bankAddress;
    private double latitude;
    private double longitude;
}
