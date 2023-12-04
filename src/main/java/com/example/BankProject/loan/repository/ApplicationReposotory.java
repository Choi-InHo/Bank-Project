package com.example.BankProject.loan.repository;

import com.example.BankProject.loan.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationReposotory extends JpaRepository<Application, Long> {
}
