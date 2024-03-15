package org.commonweb.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // 토큰 생성
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    // 토큰 검증 및 사용자 이름 반환
    public String validateTokenAndRetrieveSubject(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            //Invalid token
            return false;
        }
    }
}
