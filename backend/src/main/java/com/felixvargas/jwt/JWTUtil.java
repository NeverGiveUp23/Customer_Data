package com.felixvargas.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class JWTUtil {

    private static final String SECRET_KEY = "customer-secret-key_1234567890_customer-secret-key_1234567890_customer-secret-key_1234567890";

// allows us to set extra claims in the token
    public String issueToken(String subject){
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes){
        return issueToken(subject, Map.of("scopes", scopes));
    }


    public String issueToken(String subject, List<String> scopes){
        return issueToken(subject, Map.of("scopes", scopes));
    }


    public String issueToken(String username, Map<String, Object> claims) {
        String token = Jwts  // Java Web Token
                .builder() // builder pattern
                .setClaims(claims) // extra claims
                .setSubject(username) // subject of the token
                .setIssuer("https://customer.com") // who creates the token and signs it
                .setIssuedAt(Date.from(Instant.now())) // when the token was issued/created (now)
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // token validity
                .signWith(getKey(), SignatureAlgorithm.HS256) // signature algorithm and key
                .compact(); // compact into a string

        return token;
    }


    public String getSubject(String token){
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        Claims claims = Jwts
                .parserBuilder() // this is for parsing the token and parsing means to extract the claims from the token
                .setSigningKey(getKey()) // this means that we are going to use the same key that we used to sign the token
                .build() // this means that we are going to build the parser
                .parseClaimsJws(token) // this means that we are going to parse the token
                .getBody();// this means that we are going to get the body of the token
        return claims;
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // symmetric key
    }

    // this method is going to check if the token is valid
    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);

        return subject.equals(username) && !isTokenExpired(jwt);
    }


    // this method is going to check if the token is expired
    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());

         return getClaims(jwt).getExpiration().before(today);


    }
}
