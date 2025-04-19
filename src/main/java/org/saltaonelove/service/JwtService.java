package org.saltaonelove.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.saltaonelove.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expire-time}")
    private String expireTime;
    private ObjectMapper op = new ObjectMapper();

    public boolean isValid(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.after(new Date());
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public List<SimpleGrantedAuthority> extractRoles(String jwt) {
        Claims claims = extractAllClaims(jwt);
        List<String> roles = op.convertValue(claims.get("roles"), new TypeReference<List<String>>() {
        });
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private Claims extractAllClaims(String jwt) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
            return jwtParser.parseClaimsJws(jwt).getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid JWT: " + ex.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(
            Map<String, Object> extraClaims, User user
    ){
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.valueOf(expireTime)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
