package com.thxpapa.juneberrydiary.dto.cal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateDto {
    private Integer taskId;
    private String content;
    private Integer reward;
    private Boolean doneCd;
}
