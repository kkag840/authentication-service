package com.common.oauth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    private  Map<String, Object> payload;
    private  String accessToken;
    private  String refreshToken;
    private  Long expireInMiliSec;

}
