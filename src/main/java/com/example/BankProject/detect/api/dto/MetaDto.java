package com.example.BankProject.detect.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDto {

    @JsonProperty("total_count") // Jackson에서 제공, 제목과 변수를 매핑
    // json으로 받은 total_count값을 totalCount로 매핑
    private Integer totalCount;
}
