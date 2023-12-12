package com.example.BankProject.redisCompare.service;

import com.example.BankProject.redisCompare.vo.Product;
import com.example.BankProject.redisCompare.vo.ProductGrp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void registerMassageChair(Product product) {
        String prodGrpId = product.getProdGrpId();
        String redisKey = prodGrpId; // 변경된 부분

        // Retrieve the existing list of products from Redis
        List<Product> productList = getProductsFromRedis(redisKey);

        // Add the new product to the list
        productList.add(product);

        // Serialize the list of products to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String productListJson;
        try {
            productListJson = objectMapper.writeValueAsString(productList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing productList to JSON", e);
        }

        // Store the JSON string in Redis
        redisTemplate.opsForValue().set(redisKey, productListJson);
    }

    private List<Product> getProductsFromRedis(String redisKey) {
        String productListJson = (String) redisTemplate.opsForValue().get(redisKey);

        if (productListJson == null) {
            // If the key is not present, return an empty list
            return new ArrayList<>();
        }

        try {
            // Deserialize the JSON string to List<Product>
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(productListJson, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing productListJson", e);
        }
    }

    public List<Product> searchMassageChairByKeyword(String keyword) {
        String redisKey = keyword;
        List<Product> productList;

        String productListJson = (String) redisTemplate.opsForValue().get(redisKey);

        if (productListJson == null) {
            // 해당 그룹의 제품이 없을 경우 빈 리스트 반환
            return Collections.emptyList();
        }

        try {
            // Deserialize the JSON string to List<Product>
            ObjectMapper objectMapper = new ObjectMapper();
            productList = objectMapper.readValue(productListJson, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            // Handle the exception as needed
            throw new RuntimeException("Error deserializing productListJson", e);
        }

        // 가격순으로 정렬
        List<Product> sortedProducts = productList.stream()
                .sorted(Comparator.comparingInt(Product::getPrice))
                .collect(Collectors.toList());

        return sortedProducts;
    }
}
