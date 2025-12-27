package com.common.oauth.controller;

import com.common.oauth.model.JwtToken;
import com.common.oauth.service.JwtAuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.common.oauth.util.Constants.USER_AGENT;


@RestController
@RequestMapping("api/v1/tokens")
public class JwtTokenController {

    private final JwtAuthenticationService jwtauthenticationService;

    @Autowired
    public JwtTokenController(final JwtAuthenticationService jwtauthenticationService) {
        this.jwtauthenticationService = jwtauthenticationService;
    }

    @PostMapping
    public ResponseEntity<JwtToken> generate(@RequestBody final JwtToken token,
                                             @RequestHeader(value = USER_AGENT) String userAgent) {
        token.setUserAgent(userAgent);
        final JwtToken encodedToken = jwtauthenticationService.generateToken(token);
        return new ResponseEntity<>(encodedToken, HttpStatus.OK);
    }

    @PostMapping(path = "verify/access")
    public ResponseEntity<JwtToken> verifyAccessToken(@RequestBody final JwtToken token,@RequestHeader(value = USER_AGENT)String userAgent) {
        token.setUserAgent(userAgent);
        if (StringUtils.isBlank(token.getAccessToken())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final JwtToken decodedToken = jwtauthenticationService.verifyAccessToken(token);
        return new ResponseEntity<>(decodedToken, HttpStatus.OK);
    }

    @PostMapping(path = "refresh/access")
    public ResponseEntity<JwtToken> generateRefreshAccessToken(@RequestBody final JwtToken token,
                                                            @RequestHeader(value = USER_AGENT) String userAgent) {
        token.setUserAgent(userAgent);
        if (StringUtils.isBlank(token.getRefreshToken())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final JwtToken encodedToken = jwtauthenticationService.verifyAndGenerateAccessToken(token);
        return new ResponseEntity<>(encodedToken, HttpStatus.OK);
    }

}