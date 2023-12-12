package com.example.BankProject.redisCompare.controller;

import com.example.BankProject.redisCompare.service.LowestPriceService;
import com.example.BankProject.redisCompare.vo.Keyword;
import com.example.BankProject.redisCompare.vo.Product;
import com.example.BankProject.redisCompare.vo.ProductGrp;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final LowestPriceService lowestPriceService;

    public ProductController(LowestPriceService lowestPriceService) {
        this.lowestPriceService = lowestPriceService;
    }

    @GetMapping("/group/{groupId}")
    public String getAllProductsByGroupId(@PathVariable String groupId, Model model) {
        Set<ZSetOperations.TypedTuple<Object>> products = lowestPriceService.getAllProductsByGroupId(groupId);
        model.addAttribute("products", products);
        return "productList"; // productList.html로 이동
    }

    @GetMapping("/search/{keyword}")
    public String search(@PathVariable String keyword, Model model) {
        Keyword keywordInfo = lowestPriceService.GetLowestPriceProductByKeyword(keyword);
        model.addAttribute("keywordInfo", keywordInfo);
        return "searchResult";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @GetMapping("/compare")
    public String showLoanComparisonPage(Model model) {
        model.addAttribute("keyword", "");
        return "loanComparison";
    }

    @PostMapping("/compare")
    public String compareLoan(@RequestParam("bankName") String bankName, Model model) {
        // 검색어를 이용하여 가격 비교 정보 가져오기
        Keyword keywordInfo = lowestPriceService.GetLowestPriceProductByKeyword(bankName);
        model.addAttribute("keywordInfo", keywordInfo);
        model.addAttribute("keyword", bankName); // 검색창에 입력된 검색어를 다시 모델에 추가
        return "loanComparison";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, Model model) {
        try {
            lowestPriceService.SetNewProduct(product.getProdGrpId(), product.getProductId(), product.getPrice());
            return "redirect:/products/search/" + product.getProdGrpId();
        } catch (NullPointerException e) {
            // 예외 처리: SetNewProduct 메소드에서 NullPointerException이 발생한 경우
            model.addAttribute("error", "Error adding product: " + e.getMessage());
            return "addProduct"; // 에러 메시지와 함께 다시 addProduct 페이지로 이동
        }
    }

    @GetMapping("/addGroup")
    public String showAddProductGrpForm(Model model) {
        model.addAttribute("productGrp", new ProductGrp());
        return "addGroup";
    }

    @PostMapping("/addGroup")
    public String addProductGrp(@ModelAttribute ProductGrp productGrp) {
        int productCnt = lowestPriceService.SetNewProductGrp(productGrp);
        return "redirect:/products/search/" + productGrp.getProdGrpId();
    }
}
