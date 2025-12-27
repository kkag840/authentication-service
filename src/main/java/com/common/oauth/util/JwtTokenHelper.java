package com.common.oauth.util;

import com.common.exception.BusinessException;
import com.common.oauth.exception.ErrorCode;
import com.common.oauth.model.JwtToken;
import com.common.utils.DateUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

import static com.common.oauth.util.Constants.*;

@Slf4j
@Service
public class JwtTokenHelper {

    private final Long refreshTokenExpireTimeInMilliSec;
    private final Long mobileTokenExpireTimeInMilliSec;
    private final Long webTokenExpireTimeInMilliSec;
    private final String secret;


    @Autowired
    public JwtTokenHelper(
            @Value("${mobile.token.expire.time.in.hours:168}") final Long mobileTokenExpireTimeInHours, // Default 7 Days
            @Value("${web.token.expire.time.in.hours:2}") final Long webTokenExpireTimeInHours,
            @Value("${refresh.token.expire.time.in.hours:720}") final Long refreshTokenExpireTimeInHours, // Default 30 Days
            @Value("${jwt.secret:your_jwt_secret_key}") final String secret) {
        this.mobileTokenExpireTimeInMilliSec = mobileTokenExpireTimeInHours * MILLI_SEC_IN_ONE_HOUR;
        this.webTokenExpireTimeInMilliSec = webTokenExpireTimeInHours * MILLI_SEC_IN_ONE_HOUR;
        this.refreshTokenExpireTimeInMilliSec = refreshTokenExpireTimeInHours * MILLI_SEC_IN_ONE_HOUR;
        this.secret = secret;
    }

    public JwtToken generateToken(JwtToken token) {

        String userAgent = token.getUserAgent();

        long currentTimeInMillis = DateUtils.getCurrentTimeInUTC();
        long accessTokenExpireTimeInMilliSec;

        if (CommonUtil.isMobile(userAgent)) {
            accessTokenExpireTimeInMilliSec = currentTimeInMillis + mobileTokenExpireTimeInMilliSec;
        } else {
            accessTokenExpireTimeInMilliSec = currentTimeInMillis + webTokenExpireTimeInMilliSec;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_AGENT, userAgent);
        claims.put(CLAIMS, token.getClaims());

        String accessToken = Jwts.builder()
                .setSubject(token.getSubject())
                .setClaims(claims)
                .setIssuedAt(new Date(currentTimeInMillis))
                .setExpiration(new Date(accessTokenExpireTimeInMilliSec))
                .signWith(getSignKey(), SignatureAlgorithm.HS512).compact();

        String refreshToken = Jwts.builder()
                .setSubject(token.getSubject())
                .setClaims(claims)
                .setIssuedAt(new Date(currentTimeInMillis))
                .setExpiration(new Date(currentTimeInMillis + refreshTokenExpireTimeInMilliSec))
                .signWith(getSignKey(), SignatureAlgorithm.HS512).compact();


        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setIssuedAt(currentTimeInMillis);
        token.setExpiresAt(accessTokenExpireTimeInMilliSec);
        return token;
    }

    public Claims validateTokenAndDecode(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN_FORMAT, HttpStatus.UNAUTHORIZED);
        }
    }

    public JwtToken validateTokenAndDecode(final JwtToken jwtToken, boolean isRefresh) {

        if (jwtToken == null)
            throw new BusinessException(ErrorCode.NOT_NULL_VALIDATION, HttpStatus.BAD_REQUEST);

        String tokenStr = isRefresh
                ? jwtToken.getRefreshToken()
                : jwtToken.getAccessToken();

        Claims claims = validateTokenAndDecode(tokenStr);

        // 🔐 SUBJECT VALIDATION
        String subjectFromToken = claims.getSubject();
        if (!Objects.equals(subjectFromToken, jwtToken.getSubject())) {
            log.error("**** Subject mismatch. Expected: {}, Found: {} **** ", jwtToken.getSubject(), subjectFromToken);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        // 🔐 USER AGENT VALIDATION
        String userAgentFromToken = claims.get(USER_AGENT, String.class);
        if (!Objects.equals(userAgentFromToken, jwtToken.getUserAgent())) {
            log.error("**** UserAgent mismatch. Expected: {}, Found: {} ****", jwtToken.getUserAgent(), userAgentFromToken);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        return jwtToken;
    }

    public JwtToken verifyAndRefreshAccessToken(JwtToken jwtToken) {
        JwtToken validatedToken = validateTokenAndDecode(jwtToken, true);
        if (Objects.isNull(validatedToken))
            return null;
        return generateToken(jwtToken);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
