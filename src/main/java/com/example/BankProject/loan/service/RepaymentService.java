package com.example.BankProject.loan.service;


import com.example.BankProject.loan.domain.Application;
import com.example.BankProject.loan.domain.Entry;
import com.example.BankProject.loan.domain.Repayment;
import com.example.BankProject.loan.dto.BalanceDTO;
import com.example.BankProject.loan.dto.RepaymentDTO;
import com.example.BankProject.loan.exception.BaseException;
import com.example.BankProject.loan.exception.ResultType;
import com.example.BankProject.loan.repository.ApplicationRepository;
import com.example.BankProject.loan.repository.BalanceRepository;
import com.example.BankProject.loan.repository.EntryRepository;
import com.example.BankProject.loan.repository.RepaymentRepository;
import com.example.BankProject.loan.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceService balanceService;
    private final ModelMapper modelMapper;

    public RepaymentDTO.Response create(Long applicationId, RepaymentDTO.Request request) {

        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                        .build());
        RepaymentDTO.Response response = modelMapper.map(repayment, RepaymentDTO.Response.class);
        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    public List<RepaymentDTO.ListResponse> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);

        return repayments.stream().map(r -> modelMapper.map(r, RepaymentDTO.ListResponse.class)).collect(Collectors.toList());
    }

    public RepaymentDTO.UpdateResponse update(Long repaymentId, RepaymentDTO.Request request) {
        //validation
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                        .build()
        );

        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
                        .build()
        );
        return RepaymentDTO.UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(request.getRepaymentAmount())
                .balance(updatedBalance.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(repayment.getUpdatedAt())
                .build();

    }

    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId
                , BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
                        .build());

        repayment.setIsDeleted(true);
        repaymentRepository.save(repayment);
    }



    private boolean isRepayableApplication(Long applicationId) {
        Optional<Application> existedApplication = applicationRepository.findById(applicationId);
        if (existedApplication.isEmpty()) {
            return false;
        }

        if (existedApplication.get().getContractedAt() == null) {
            return false;
        }

        //집행이 됐는지도 확인 해야경
        Optional<Entry> existedEntry = entryRepository.findByApplicationID(applicationId);
        return existedEntry.isPresent();
    }
}




