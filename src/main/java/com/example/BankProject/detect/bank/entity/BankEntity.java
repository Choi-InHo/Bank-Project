package com.example.BankProject.detect.bank.entity;

import com.example.BankProject.detect.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;
    private String bankAddress;
    private double latitude;
    private double longitude;

    public void changeBankAddress(String address){
        this.bankAddress = address;

    }

}
