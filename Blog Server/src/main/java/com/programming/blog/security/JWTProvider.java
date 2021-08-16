package com.programming.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;

//constructs JWT and generates associated key
@Service
public class JWTProvider {

    private Key key;

    //generate key with which to sign JWTs
    @PostConstruct
    public void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    //generate JWT
    public String generateToken(Authentication authentication) {
        //built in User object created from Spring Security - not User object from the model package
        User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(key)
                .compact();
    }

    //check if token is valid
    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);   //if executed without errors, will return true?
        return true;
    }

    //extract username from the JWT
    public String getUsernameFromJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
