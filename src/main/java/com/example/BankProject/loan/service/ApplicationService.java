package com.example.BankProject.loan.service;


import com.example.BankProject.loan.domain.Application;
import com.example.BankProject.loan.dto.ApplicationDTO;
import com.example.BankProject.loan.exception.BaseException;
import com.example.BankProject.loan.exception.ResultType;
import com.example.BankProject.loan.repository.ApplicationRepository;
import com.example.BankProject.loan.repository.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JudgmentRepository judgmentRepository;
    private final ModelMapper modelMapper;

    public ApplicationDTO.Response create(ApplicationDTO.Request request) {
        Application application = modelMapper.map(request, Application.class);

        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, ApplicationDTO.Response.class);
    }

    public ApplicationDTO.Response update(Long applicationId, ApplicationDTO.Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, ApplicationDTO.Response.class);
    }

    public ApplicationDTO.Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(application, ApplicationDTO.Response.class);
    }

    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setIsDeleted(true);

        applicationRepository.save(application);


    }


    @Transactional
    public ApplicationDTO.Response contract(Long applicationId) {
        //신청 정보가 있는지
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        //심사 정보가 있는지
        judgmentRepository.findByApplicationId(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        //승인된 금액이 NOT NULL 이거나 0원보다 높은지
        if(application.getApprovalAmount() == null || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        application.setContractedAt(LocalDateTime.now());
        Application updated = applicationRepository.save(application);

        return modelMapper.map(updated, ApplicationDTO.Response.class);
    }
}
