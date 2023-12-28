package com.thxpapa.juneberrydiary.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {
    private String name;
    private String email;
    private String username;
    private String password;
    private String intro;
}
