package com.example.BankProject.detect.bank.repository;

import com.example.BankProject.detect.bank.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankEntity, Long> {
}
