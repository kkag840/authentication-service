package com.common.oauth.service;

import com.common.oauth.model.JwtToken;
import com.common.oauth.util.JwtTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class JwtAuthenticationService {
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public JwtAuthenticationService(final JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public JwtToken generateToken(final JwtToken jwtToken) {
        log.info("******  JwtAuthenticationService ::: generateToken :: subject : {} ,  userAgent : {} ", jwtToken.getSubject(),jwtToken.getUserAgent() );
        return jwtTokenHelper.generateToken(jwtToken);
    }

    /**
     * This function verify access JwtToken and provide JwtToken detail;
     *
     * @param jwtToken then {@link JwtToken} access JwtToken.
     * @return the {@link JwtToken} JwtToken details.
     */
    public JwtToken verifyAccessToken(final JwtToken jwtToken) {
        log.info("******  JwtAuthenticationService ::: verifyAccessToken :: subject : {} ,  userAgent : {}, accessToken : {} ", jwtToken.getSubject(), jwtToken.getUserAgent(), jwtToken.getAccessToken() );
        return jwtTokenHelper.validateTokenAndDecode(jwtToken,false);
    }

    /**
     * This function verify refresh JwtToken and generate new access JwtToken.
     *
     * @param jwtToken the {@link JwtToken} refresh JwtToken.
     * @return then {@link JwtToken} access JwtToken.
     */

    public JwtToken verifyAndGenerateAccessToken(final JwtToken jwtToken) {
        log.info("******  JwtAuthenticationService ::: verifyAndGenerateAccessToken :: subject : {} ,  userAgent : {}, refreshToken : {} ", jwtToken.getSubject(),jwtToken.getUserAgent(), jwtToken.getRefreshToken() );
        return jwtTokenHelper.verifyAndRefreshAccessToken(jwtToken);
    }

}
