package com.codingshuttle.youtube.hospitalManagement.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private  final PasswordEncoder passwordEncoder;

    private  String key ="sdjknvfirvjsfkeflijdilhrbeldfijalihrwnfdlivrwhefidhciorsfheruhfivrg";

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .signWith(secretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .compact();
    }

    public String getUsername(String token) {
         return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();

    }
}
