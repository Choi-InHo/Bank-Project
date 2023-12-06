package com.example.BankProject.loan.controller;


import com.example.BankProject.domain.constant.SearchType;
import com.example.BankProject.dto.response.ArticleResponse;
import com.example.BankProject.loan.dto.CounselDTO;
import com.example.BankProject.loan.dto.ResponseDTO;
import com.example.BankProject.loan.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.BankProject.loan.dto.ResponseDTO.ok;

import com.example.BankProject.loan.dto.CounselDTO;
import com.example.BankProject.loan.service.CounselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/counsels")
public class CounselController {

    private final CounselService counselService;

    @Autowired
    public CounselController(CounselService counselService) {
        this.counselService = counselService;
    }

    // 상담 목록 조회 페이지
    @GetMapping("/list")
    public String getCounselList(Model model) {
        List<CounselDTO.Response> counselList = counselService.getAllCounsels();
        model.addAttribute("counselList", counselList);
        return "counselList";
    }

    // 상담 등록 페이지
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Add an empty CounselDTO.Request object to the model
        model.addAttribute("counselDetails", new CounselDTO.Request());
        return "createCounsel";
    }

    // 상담 상세 정보 조회 페이지
    @GetMapping("/details/{counselId}")
    public String getCounselDetails(@PathVariable Long counselId, Model model) {
        CounselDTO.Response counselDetails = counselService.get(counselId);
        model.addAttribute("counselDetails", counselDetails);
        return "counselDetails";
    }

    // 상담 업데이트 페이지
    @GetMapping("/update/{counselId}")
    public String getUpdateCounselPage(@PathVariable Long counselId, Model model) {
        CounselDTO.Response counselDetails = counselService.get(counselId);
        model.addAttribute("counselDetails", counselDetails);
        return "updateCounsel";
    }

    // 상담 등록 처리
    @PostMapping("/create")
    public String create(@ModelAttribute("counselDetails") CounselDTO.Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createCounsel"; // Return the view if there are validation errors
        }

        counselService.create(request);
        return "redirect:/counsels/list"; // Redirect to the list page after successful creation
    }

    // 상담 업데이트 처리
    @PostMapping("/update/{counselId}")
    public String updateCounsel(
            @PathVariable Long counselId,
            @ModelAttribute CounselDTO.Request request) {
        counselService.update(counselId, request);
        // 업데이트 후 상세 정보 페이지로 리다이렉트
        return "redirect:/counsels/details/" + counselId;
    }

    // 상담 삭제 처리
    @PostMapping("/delete/{counselId}")
    public String deleteCounsel(@PathVariable Long counselId) {
        counselService.delete(counselId);
        return "redirect:/counsels/list";
    }
}

