package com.example.BankProject.detect.bank.controller;


import com.example.BankProject.detect.bank.cache.BankRedisTemplateService;
import com.example.BankProject.detect.bank.dto.BankDto;
import com.example.BankProject.detect.bank.service.BankRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BankController {

    private final BankRepositoryService bankRepositoryService;
    private final BankRedisTemplateService bankRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {
        List<BankDto> bankDtoList = bankRepositoryService.findAll()
                .stream().map(bankEntity -> BankDto.builder()
                        .id(bankEntity.getId())
                        .bankName(bankEntity.getBankName())
                        .bankAddress(bankEntity.getBankAddress())
                        .latitude(bankEntity.getLatitude())
                        .longitude(bankEntity.getLongitude())
                        .build())
                .collect(Collectors.toList());

        bankDtoList.forEach(bankRedisTemplateService::save);
        return "success";
    }
}
