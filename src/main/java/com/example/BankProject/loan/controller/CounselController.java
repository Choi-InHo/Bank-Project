package com.example.BankProject.loan.controller;


import com.example.BankProject.loan.dto.CounselDTO;
import com.example.BankProject.loan.dto.ResponseDTO;
import com.example.BankProject.loan.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.BankProject.loan.dto.ResponseDTO.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsels")
public class CounselController {

    private final CounselService counselService;

    @PostMapping
    public ResponseDTO<CounselDTO.Response> create(@RequestBody CounselDTO.Request request) {
        return ok(counselService.create(request));
    }

    @GetMapping("/{counselId}")
    public ResponseDTO<CounselDTO.Response> get(@PathVariable Long counselId) {
        return ok(counselService.get(counselId));
    }

    @PutMapping("/{counselId}")
    public ResponseDTO<CounselDTO.Response> update(@PathVariable Long counselId, @RequestBody CounselDTO.Request request) {
        return ok(counselService.update(counselId, request));
    }

    @DeleteMapping("/{counselId}")
    public ResponseDTO<Void> delete(@PathVariable Long counselId) {
        counselService.delete(counselId);
        return ok();
    }


}
