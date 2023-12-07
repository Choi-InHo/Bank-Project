package com.example.BankProject.loan.controller;


import com.example.BankProject.loan.dto.ApplicationDTO;
import com.example.BankProject.loan.dto.JudgmentDTO;
import com.example.BankProject.loan.dto.ResponseDTO;
import com.example.BankProject.loan.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/judgments")
public class JudgmentController  {

    private final JudgmentService judgmentService;

    @GetMapping("/create/{applicationId}")
    public String showCreateJudgmentForm(@PathVariable Long applicationId, Model model) {
        // 여기서 applicationId를 모델에 담아서 뷰로 전달합니다.
        model.addAttribute("applicationId", applicationId);

        return "createJudgmentForm";
    }

    @PostMapping("/create")
    public String createJudgment(JudgmentDTO.Request request) {
        JudgmentDTO.Response response = judgmentService.create(request);
        Long applicationId = request.getApplicationId();
        return "createJudgmentForm";
    }

    @GetMapping("/view/{applicationId}")
    public String viewJudgment(@PathVariable Long applicationId, Model model) {
        // 해당 Application의 Judgment 정보를 가져옵니다.
        JudgmentDTO.Response judgment = judgmentService.getJudgmentOfApplication(applicationId);

        // 가져온 Judgment 정보를 모델에 담아 뷰로 전달합니다.
        model.addAttribute("judgment", judgment);

        return "viewJudgment"; // viewJudgment.html로 매핑됩니다.
    }

//    @PostMapping("/create")
//    public ResponseDTO<JudgmentDTO.Response> createJudgment(@RequestBody JudgmentDTO.Request request) {
//        return ok(judgmentService.create(request));
//    }
//
//    @GetMapping("/view/{applicationId}")
//    public ResponseDTO<JudgmentDTO.Response> viewJudgment(@PathVariable Long applicationId) {
//        return ok(judgmentService.getJudgmentOfApplication(applicationId));
//    }
//
//    @PostMapping
//    public ResponseDTO<JudgmentDTO.Response> create(@RequestBody JudgmentDTO.Request request) {
//        return ok(judgmentService.create(request));
//    }
//
//    @GetMapping("/{judgmentId}")
//    public ResponseDTO<JudgmentDTO.Response> get(@PathVariable Long judgmentId) {
//        return ok(judgmentService.get(judgmentId));
//    }
//
//    @PutMapping("/{judgmentId}")
//    public ResponseDTO<JudgmentDTO.Response> update(@PathVariable Long judgmentId, @RequestBody JudgmentDTO.Request request) {
//        return ok(judgmentService.update(judgmentId, request));
//    }
//
//    @DeleteMapping("/{judgmentId}")
//    public ResponseDTO<Void> delete(@PathVariable Long judgmentId) {
//        judgmentService.delete(judgmentId);
//        return ok();
//    }
//
//    @PatchMapping("/{judgmentId}/grants")
//    public ResponseDTO<ApplicationDTO.GrantAmount> grant(@PathVariable Long judgmentId) {
//        return ok(judgmentService.grant(judgmentId));
//    }
}