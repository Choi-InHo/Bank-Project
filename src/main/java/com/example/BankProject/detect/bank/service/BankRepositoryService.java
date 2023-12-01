package com.example.BankProject.detect.bank.service;


import com.example.BankProject.detect.bank.entity.BankEntity;
import com.example.BankProject.detect.bank.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankRepositoryService {
    private final BankRepository bankRepository;

    @Transactional
    public void updateAddress(Long id, String address) {
        BankEntity entity = bankRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("not found");
            return;
        }

        entity.changeBankAddress(address);
    }

    @Transactional
    public List<BankEntity> findAll() {
        return bankRepository.findAll();
    }
}
