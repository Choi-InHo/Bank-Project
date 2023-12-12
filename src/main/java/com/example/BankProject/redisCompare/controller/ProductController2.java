package com.example.BankProject.redisCompare.controller;

import com.example.BankProject.redisCompare.service.ProductService;
import com.example.BankProject.redisCompare.vo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showSearchForm() {
        return "searchForm";
    }

    @PostMapping("/search")
    public String searchMassageChair(@RequestParam String keyword, Model model) {
        model.addAttribute("products", productService.searchMassageChairByKeyword(keyword));
        return "searchResults";
    }
}
