package com.common.oauth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtToken {

    private String subject;        // userId / adminId / systemId
    private String userAgent;
    private String accessToken;
    private String refreshToken;
    private Long issuedAt;
    private Long expiresAt;
    private Map<String, Object> claims;

}
