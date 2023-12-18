package com.example.BankProject.redisCompare.controller;

import com.example.BankProject.redisCompare.service.ProductService;
import com.example.BankProject.redisCompare.vo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/massage-chair")
public class ProductController2 {

    @Autowired
    private ProductService productService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerMassageChair(
            @RequestParam String prodGrpId,
            @RequestParam String productId,
            @RequestParam int price
    ) {
        Product product = new Product();
        product.setProdGrpId(prodGrpId);
        product.setProductId(productId);
        product.setPrice(price);

        productService.registerMassageChair(product);
        return "redirect:/massage-chair/register";
    }


    @GetMapping("/search")
    public String showSearchFormAndResults(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null) {
            model.addAttribute("products", productService.searchMassageChairByKeyword(keyword));
        }
        return "searchFormAndResults";
    }

    @PostMapping("/search")
    public String searchMassageChair(@RequestParam String keyword) {
        System.out.println("Searching for keyword: " + keyword);
        String encodedKeyword = UriUtils.encodeQueryParam(keyword, StandardCharsets.UTF_8);
        // Redirect to the same URL to show results on the same page
        return "redirect:/massage-chair/search?keyword=" + encodedKeyword;
    }
}
