package com.example.BankProject.loan.repository;

import com.example.BankProject.loan.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    Optional<Entry> findByApplicationID(Long applicationId);
}
