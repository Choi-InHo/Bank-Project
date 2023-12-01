package com.example.BankProject.detect.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {

    private String BankName; // 병원명
    private String BankAddress; // 약국주소
    private String directionUrl; //길안내 url
    private String roadViewUrl; //로드뷰 url
    private String distance; //고객 주소와 약국 주소의 거리
}
