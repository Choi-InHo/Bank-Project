package com.example.BankProject.detect.bank.service;


import com.example.BankProject.detect.bank.cache.BankRedisTemplateService;
import com.example.BankProject.detect.bank.dto.BankDto;
import com.example.BankProject.detect.bank.entity.BankEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankSearchService {

    private final BankRepositoryService bankRepositoryService;
    private final BankRedisTemplateService bankRedisTemplateService;


    public List<BankDto> searchBankDtoList() {

        //redis
        List<BankDto> bankDtoList = bankRedisTemplateService.findAll();
        if(!bankDtoList.isEmpty()){
            log.info("redis findAll success!");
            return bankDtoList;
        }

        // db
        return bankRepositoryService.findAll()
                .stream()
                .map(this::convertBankDto).
                collect(Collectors.toList());
    }

    private BankDto convertBankDto(BankEntity bank) {
        return BankDto.builder()
                .id(bank.getId())
                .bankAddress(bank.getBankAddress())
                .bankName(bank.getBankName())
                .latitude(bank.getLatitude())
                .longitude(bank.getLongitude())
                .build();
    }

}
