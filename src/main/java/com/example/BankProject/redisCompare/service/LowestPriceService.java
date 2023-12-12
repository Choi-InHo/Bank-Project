package com.example.BankProject.redisCompare.service;

import com.example.BankProject.redisCompare.vo.Keyword;
import com.example.BankProject.redisCompare.vo.Product;
import com.example.BankProject.redisCompare.vo.ProductGrp;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface LowestPriceService {

    Set GetZsetValue(String key);

    Set GetZsetValueWithStatus(String key) throws Exception;

    Set GetZsetValueWithSpecificException(String key) throws Exception;

    void SetNewProduct(String groupId, String productName, double price);
    int SetNewProductGrp(ProductGrp newProductGrp);

    int SetNewProductGrpToKeyword(String keyword, String prodGrpId, double score);

    public Set<ZSetOperations.TypedTuple<Object>> getAllProductsByGroupId(String groupId);

    Keyword GetLowestPriceProductByKeyword(String keyword);

}
