package com.example.BankProject.detect.bank.service;


import com.example.BankProject.detect.api.dto.DocumentDto;
import com.example.BankProject.detect.api.dto.KakaoApiResponseDto;
import com.example.BankProject.detect.api.service.KakaoAddressSearchService;
import com.example.BankProject.detect.direction.dto.OutputDto;
import com.example.BankProject.detect.direction.entity.Direction;
import com.example.BankProject.detect.direction.repository.DirectionRepository;
import com.example.BankProject.detect.direction.service.Base62Service;
import com.example.BankProject.detect.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankRecommendationService {
    private final DirectionRepository directionRepository;

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;

    public List<OutputDto> recommendBankList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())){
            log.error("찾을 수 없음");
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // 공공기관 은행 데이터 및 거리계산 알고리즘 이용
        List<Direction> directionList = directionService.buildDirectionList(documentDto);

        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction){
        return OutputDto.builder()
                .BankName(direction.getTargetBankName())
                .BankAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
