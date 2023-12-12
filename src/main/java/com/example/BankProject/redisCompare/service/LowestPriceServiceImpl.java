package com.example.BankProject.redisCompare.service;

import com.example.BankProject.redisCompare.vo.Keyword;
import com.example.BankProject.redisCompare.vo.NotFoundException;
import com.example.BankProject.redisCompare.vo.Product;
import com.example.BankProject.redisCompare.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LowestPriceServiceImpl implements LowestPriceService{

    private final RedisTemplate myProdPriceRedis;


    @Override
    public Set GetZsetValue(String key) {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        // 우리가 갖고 있는게 Zset(sort set)이기 때문에 opsForZset()으로 만듬


        return myTempSet;
    }

    public Set<ZSetOperations.TypedTuple<Object>> getAllProductsByGroupId(String groupId) {
        return myProdPriceRedis.opsForZSet().rangeWithScores(groupId, 0, -1);
    }

    @Override
    public Set GetZsetValueWithStatus(String key) throws Exception {

        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if(myTempSet.size() < 1) {
            throw new Exception("The key doesn't have any memeber");
        }
        return myTempSet;
    }

    @Override
    public Set GetZsetValueWithSpecificException(String key) throws Exception {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0,9 );
        if(myTempSet.size() < 1) {
            throw new NotFoundException("not exist in redis", HttpStatus.NOT_FOUND);
        }
        return null;
    }

    @Override
    public void SetNewProduct(String groupId, String productName, double price) {
        ZSetOperations<String, Object> zSetOps = myProdPriceRedis.opsForZSet();

        // 상품의 현재 순위를 가져옴
        Long currentRank = zSetOps.rank(groupId, productName);

        if (currentRank == null) {
            // 상품이 존재하지 않으면 추가
            zSetOps.add(groupId, productName, price);
        } else {
            // 상품이 이미 존재하면 업데이트
            zSetOps.add(groupId, productName, price);
        }
    }

    @Override
    public int SetNewProductGrp(ProductGrp newProductGrp) {
        List<Product> product = newProductGrp.getProductList();

        // Check if the product list is not null and not empty
        if (product != null && !product.isEmpty()) {
            String productId = product.get(0).getProductId();
            double price = product.get(0).getPrice();

            // Now you can use productId in the add method
            myProdPriceRedis.opsForZSet().add(newProductGrp.getProdGrpId(), productId, price);

            // Rest of your logic...
            int productCnt = myProdPriceRedis.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue();
            return productCnt;
        } else {
            // Handle the case where the product list is null or empty
            return 0; // Or return an appropriate value
        }
    }

    @Override
    public int SetNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);
        return myProdPriceRedis.opsForZSet().rank(keyword,prodGrpId).intValue();
    }

    @Override
    public Keyword GetLowestPriceProductByKeyword(String keyword) {
        Keyword returnInfo =  new Keyword();
        List<ProductGrp> tempProdGrp = new ArrayList<>();
        //keyword를 통해 ProductGrop가져오기(10개)
        tempProdGrp = GetProdGrpUsingKeyword(keyword);

        //가져온 정보들을 Return 할 Object에 넣기
        returnInfo.setKeyword(keyword);
        returnInfo.setProductGrpList(tempProdGrp);
        return returnInfo;
    }
    public List<Product> GetProductsByBank(String bankName) {
        List<Product> returnInfo = new ArrayList<>();

        // input 받은 bankName으로 productGrpId를 조회
        List<String> prodGrpIdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(bankName, 0, 9));

        //10개 prodGrpId로 loop
        for (final String prodGrpId : prodGrpIdList) {
            // Loop 타면서 ProductGrpID로 Product:price 가져오기 (10개)
            Set prodAndPriceList = myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);
            Iterator<Object> prodPricObj = prodAndPriceList.iterator();

            // loop 타면서 product obj에 bind (10개)
            while (prodPricObj.hasNext()) {
                ObjectMapper objMapper = new ObjectMapper();
                // {"value":00-10111-}, {"score":11000}
                Map<String, Object> prodPriceMap = objMapper.convertValue(prodPricObj.next(), Map.class);
                Product tempProduct = new Product();
                // Product Obj bind
                tempProduct.setProductId(prodPriceMap.get("value").toString()); // prod_id
                tempProduct.setPrice(Double.valueOf(prodPriceMap.get("score").toString()).intValue()); //es 검색된 score
                tempProduct.setProdGrpId(prodGrpId);

                returnInfo.add(tempProduct);
            }
        }

        // 가격에 따라 정렬
        returnInfo.sort(Comparator.comparing(Product::getPrice));

        return returnInfo;
    }

    public List<ProductGrp> GetProdGrpUsingKeyword(String keyword) {

        List<ProductGrp> returnInfo = new ArrayList<>();

        // input 받은 keyword 로 productGrpId를 조회
        List<String> prodGrpIdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(keyword, 0, 9));
        //Product tempProduct = new Product();
        List<Product> tempProdList = new ArrayList<>();

        //10개 prodGrpId로 loop
        for (final String prodGrpId : prodGrpIdList) {
            // Loop 타면서 ProductGrpID 로 Product:price 가져오기 (10개)

            ProductGrp tempProdGrp = new ProductGrp();

            Set prodAndPriceList = new HashSet();
            //여기서 rangewithScores에서 socres는 price가 아닌 keyword와 더 적합한 걸 보여줄 수 있도록함(1~2)
            prodAndPriceList = myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);
            Iterator<Object> prodPricObj = prodAndPriceList.iterator();

            // loop 타면서 product obj에 bind (10개)
            while (prodPricObj.hasNext()) {
                ObjectMapper objMapper = new ObjectMapper();
                // {"value":00-10111-}, {"score":11000}
                Map<String, Object> prodPriceMap = objMapper.convertValue(prodPricObj.next(), Map.class);
                Product tempProduct = new Product();
                // Product Obj bind
                tempProduct.setProductId(prodPriceMap.get("value").toString()); // prod_id
                tempProduct.setPrice(Double.valueOf(prodPriceMap.get("score").toString()).intValue()); //es 검색된 score
                tempProduct.setProdGrpId(prodGrpId);

                tempProdList.add(tempProduct);
            }
            // 10개 product price 입력완료
            tempProdGrp.setProdGrpId(prodGrpId);
            tempProdGrp.setProductList(tempProdList);
            returnInfo.add(tempProdGrp);
        }

        return returnInfo;
    }

    public void DeleteKey(String key) {
        myProdPriceRedis.delete(key);
    }
}
