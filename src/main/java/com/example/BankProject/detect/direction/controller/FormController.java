package com.example.BankProject.detect.direction.controller;


import com.example.BankProject.detect.bank.service.BankRecommendationService;
import com.example.BankProject.detect.direction.dto.InputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class FormController {
    private final BankRecommendationService bankRecommendationService;

    @GetMapping("/detect")
    public String main2(){
        return "main";
    }

    @PostMapping("/search")
    public ModelAndView postDirection(@ModelAttribute InputDto inputDto){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output");
        modelAndView.addObject("outputFormList", bankRecommendationService.recommendBankList(inputDto.getAddress()));

        return modelAndView;
    }
}
