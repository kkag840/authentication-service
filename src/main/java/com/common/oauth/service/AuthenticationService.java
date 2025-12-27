package com.common.oauth.service;

import com.common.oauth.model.Token;
import com.common.oauth.util.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    private final TokenHelper tokenHelper;

    @Autowired
    public AuthenticationService(final TokenHelper tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    /**
     * Generate a new token (access + refresh) from payload.
     *
     * @param payload the key-value payload to include in token
     * @param isMobile whether the client is mobile
     * @return generated Token
     */
    public Token generateToken(final Map<String, Object> payload, boolean isMobile) {
        return tokenHelper.generateToken(payload, isMobile);
    }

    /**
     * Verify access token and decode its payload.
     *
     * @param accessToken the encrypted access token
     * @return decoded payload map, null if invalid/expired
     */
    public Map<String, Object> verifyAndDecode(final String accessToken) {
        return tokenHelper.decodeToken(accessToken);
    }

    /**
     * Verify refresh token and generate new access token.
     *
     * @param refreshToken the encrypted refresh token
     * @param isMobile whether the client is mobile
     * @return new Token with updated access token
     */
    public Token verifyAndGenerate(final String refreshToken, boolean isMobile) {
        Map<String, Object> payload = tokenHelper.decodeToken(refreshToken);
        if (payload == null) {
            return null; // invalid or expired refresh token
        }
        return tokenHelper.refreshToken(payload, isMobile);
    }
}
