package com.common.oauth.util;

import com.common.oauth.model.Token;
import com.common.oauth.service.AESCryptography;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHelper.class);

    private static final Long MILLISEC_IN_ONE_HOUR = 3600000L;

    private final AESCryptography aesCryptography;
    private final Long refreshTokenExpireTimeInMiliSec;
    private final Long mobileTokenExpireTimeInMiliSec;
    private final Long webTokenExpireTimeInMiliSec;

    @Autowired
    public TokenHelper(final AESCryptography aesCryptography,
                       @Value("${mobile.token.expire.time.in.hours:1440}") final Long mobileTokenExpireTimeInHours,
                       @Value("${web.token.expire.time.in.hours:8}") final Long webTokenExpireTimeInHours,
                       @Value("${refresh.token.expire.time.in.hours:1440}") final Long refreshTokenExpireTimeInHours) {
        this.aesCryptography = aesCryptography;
        this.mobileTokenExpireTimeInMiliSec = mobileTokenExpireTimeInHours * MILLISEC_IN_ONE_HOUR;
        this.webTokenExpireTimeInMiliSec = webTokenExpireTimeInHours * MILLISEC_IN_ONE_HOUR;
        this.refreshTokenExpireTimeInMiliSec = refreshTokenExpireTimeInHours * MILLISEC_IN_ONE_HOUR;
    }

    // Generate Access & Refresh Tokens
    public Token generateToken(Map<String, Object> payload, boolean isMobile) {
        long expireTime = Instant.now().toEpochMilli() + (isMobile ? mobileTokenExpireTimeInMiliSec : webTokenExpireTimeInMiliSec);
        payload.put("expireTime", expireTime);

        String tokenString = mapToString(payload);
        String accessToken = aesCryptography.encrypt(tokenString);
        String refreshToken = aesCryptography.encrypt("refresh_" + tokenString);

        return new Token(payload, accessToken, refreshToken, expireTime);
    }

    // Generate Refresh Token separately if needed
    public String generateRefreshToken(Map<String, Object> payload) {
        payload.put("expireTime", Instant.now().toEpochMilli() + refreshTokenExpireTimeInMiliSec);
        String tokenString = mapToString(payload);
        return aesCryptography.encrypt("refresh_" + tokenString);
    }

    // Decode any token
    public Map<String, Object> decodeToken(String encryptedToken) {
        try {
            String decryptedString = aesCryptography.decrypt(encryptedToken);
            // Remove "refresh_" prefix if exists
            if (decryptedString.startsWith("refresh_")) {
                decryptedString = decryptedString.substring(8);
            }

            Map<String, Object> payload = new HashMap<>();
            for (String pair : decryptedString.split("&")) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    payload.put(keyValue[0], keyValue[1]);
                }
            }

            // Validate expiration
            if (payload.containsKey("expireTime")) {
                long expireTime = Long.parseLong(payload.get("expireTime").toString());
                if (Instant.now().toEpochMilli() > expireTime) {
                    LOGGER.info("Token expired");
                    return null;
                }
            }

            return payload;
        } catch (Exception e) {
            LOGGER.error("Invalid token: {}", e.getMessage());
            return null;
        }
    }

    // Refresh Access Token using payload
    public Token refreshToken(Map<String, Object> payload, boolean isMobile) {
        return generateToken(payload, isMobile);
    }

    // Helper: convert Map to string for encryption
    private String mapToString(Map<String, Object> payload) {
        return payload.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");
    }
}
