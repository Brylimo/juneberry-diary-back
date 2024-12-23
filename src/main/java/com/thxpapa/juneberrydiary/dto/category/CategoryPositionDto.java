package com.thxpapa.juneberrydiary.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPositionDto {
    private Long categoryUid;
    private Integer position;
}
