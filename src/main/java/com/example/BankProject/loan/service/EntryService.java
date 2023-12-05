package com.example.BankProject.loan.service;


import com.example.BankProject.loan.domain.Application;
import com.example.BankProject.loan.domain.Entry;
import com.example.BankProject.loan.dto.BalanceDTO;
import com.example.BankProject.loan.dto.EntryDTO;
import com.example.BankProject.loan.exception.BaseException;
import com.example.BankProject.loan.exception.ResultType;
import com.example.BankProject.loan.repository.ApplicationReposotory;
import com.example.BankProject.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final ApplicationReposotory applicationReposotory;
    private final ModelMapper modelMapper;
    private final BalanceService balanceService;


    @Transactional
    public EntryDTO.Response create(Long applicationId, EntryDTO.Request request) {
        //계약 체결 여부 검증
        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationID(applicationId);

        entryRepository.save(entry);

//        //대출 잔고 관리
//        balanceService.create(applicationId,
//                BalanceDTO.CreateRequest.builder()
//                        .entryAmount(request.getEntryAmount())
//                        .build()
//        );

        return modelMapper.map(entry, EntryDTO.Response.class);
    }

    public EntryDTO.Response get(Long applicationId) {
        Entry entry = entryRepository.findByApplicationID(applicationId).orElseThrow();
        return modelMapper.map(entry, EntryDTO.Response.class);
    }

    public EntryDTO.UpdateResponse update(Long entryId, EntryDTO.Request request) {
        // entry
        Entry entry = entryRepository.findById(entryId).orElseThrow();

        //before -> after
        BigDecimal beforeEntryAmount = entry.getEntryAmount();
        entry.setEntryAmount(request.getEntryAmount());

        entryRepository.save(entry);

        Long applicationId = entry.getApplicationID();
        balanceService.update(applicationId,
                BalanceDTO.UpdateRequest.builder()
                        .beforeEntryAmount(beforEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .builder()
                );
        return EntryDTO.UpdateResponse.builder()
                .applicationId(entry.getApplicationID())
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(entry.getEntryAmount())
                .build();
    }

    @Transactional
    public void delete(Long entryId){
        Entry entry = entryRepository.findById(entryId).orElseThrow(()-> new BaseException(ResultType.SYSTEM_ERROR));

        entry.setIsDeleted(true);

        entryRepository.save(entry);

        BigDecimal beforeEntryAmount = entry.getEntryAmount();

        Long applicationId = entry.getApplicationID();
        balanceService.update(applicationId,
                BalanceDTO.UpdateRequest.builder()
                        .beforeAmount(beforeEntryAmount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build()
        );
    }

    private boolean isContractedApplication(Long applicationId){
        Optional<Application> existed = applicationReposotory.findById(applicationId);

        if (existed.isEmpty()) {
            return false;
        }

        return existed.get().getContractedAt() != null;
    }
}
