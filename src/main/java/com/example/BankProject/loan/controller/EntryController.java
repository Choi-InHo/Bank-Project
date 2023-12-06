//package com.example.BankProject.loan.controller;
//
//import com.example.BankProject.loan.dto.EntryDTO;
//import com.example.BankProject.loan.dto.RepaymentDTO;
//import com.example.BankProject.loan.dto.ResponseDTO;
//import com.example.BankProject.loan.service.EntryService;
//import com.example.BankProject.loan.service.RepaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/internal/applications")
//@RequiredArgsConstructor
//public class EntryController {
//
//    private final EntryService entryService;
//    private final RepaymentService repaymentService;
//
//    @GetMapping("/{applicationId}/entries/create")
//    public String showEntryForm(@PathVariable Long applicationId, Model model) {
//        model.addAttribute("applicationId", applicationId);
//        return "entryForm";
//    }
//
//    @PostMapping("/{applicationId}/entries/create")
//    public String createEntry(@PathVariable Long applicationId, EntryDTO.Request request) {
//        entryService.create(applicationId, request);
//        return "redirect:/internal/applications/entries/" + applicationId;
//    }
//
//    @GetMapping("/entries/{entryId}")
//    public String getEntryDetails(@PathVariable Long entryId, Model model) {
//        EntryDTO.Response entryDetails = entryService.get(entryId);
//        model.addAttribute("entryDetails", entryDetails);
//        return "entryDetails";
//    }
//
//    @GetMapping("/{applicationId}/entries/{entryId}/update")
//    public String showUpdateForm(@PathVariable Long applicationId, @PathVariable Long entryId, Model model) {
//        EntryDTO.Response entryDetails = entryService.get(entryId);
//        model.addAttribute("applicationId", applicationId);
//        model.addAttribute("entryDetails", entryDetails);
//        return "updateEntryForm";
//    }
//
//    @PostMapping("/{applicationId}/entries/{entryId}/update")
//    public String updateEntry(@PathVariable Long entryId, EntryDTO.Request request) {
//        entryService.update(entryId, request);
//        return "redirect:/internal/applications/entries/" + entryId;
//    }
//
//    @PostMapping("/entries/{entryId}/delete")
//    public String deleteEntry(@PathVariable Long entryId) {
//        entryService.delete(entryId);
//        return "redirect:/internal/applications";
//    }
//
//    // Repayment Controller 부분은 비슷한 방식으로 추가하시면 됩니다.
//}
