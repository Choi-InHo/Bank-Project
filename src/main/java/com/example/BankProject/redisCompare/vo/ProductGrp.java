package com.example.BankProject.redisCompare.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGrp {

    private String GrpId;
    private List<Product> productList;
}
