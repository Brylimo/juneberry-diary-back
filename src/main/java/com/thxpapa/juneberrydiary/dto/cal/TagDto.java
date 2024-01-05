package com.thxpapa.juneberrydiary.dto.cal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    private LocalDate date;
    private String name;
    private String tagType;
}
