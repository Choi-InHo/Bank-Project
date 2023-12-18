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
            @RequestParam String price,
            @RequestParam String imageUrl,
            @RequestParam String productUrl
    ) {
        Product product = new Product();
        product.setProdGrpId(prodGrpId);
        product.setProductId(productId);
        try {
            // 문자열로 받은 price를 double로 변환
            double priceValue = Double.parseDouble(price);
            product.setPrice(priceValue);
        } catch (NumberFormatException e) {
            // 예외 처리: 숫자로 변환할 수 없는 경우
            // 원하는 방식으로 처리하거나 오류 메시지를 추가할 수 있음
            e.printStackTrace();
        }

        product.setImageUrl(imageUrl);
        product.setProductUrl(productUrl);

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
