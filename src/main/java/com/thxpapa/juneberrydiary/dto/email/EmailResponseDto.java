package com.thxpapa.juneberrydiary.dto.email;

import lombok.*;

public class EmailResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailMessage {
        private String to;
        private String subject;
        private String message;
    }
}
