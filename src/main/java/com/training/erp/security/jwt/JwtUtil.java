package com.training.erp.security.jwt;



import com.tms.service.serviceImpl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.training.erp.util.UtilProperties.APPLICATION_SECRET_KEY;
import static com.training.erp.util.UtilProperties.TOKEN_EXPIRATION_TIME;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public String generateToken(Authentication authentication){

        UserDetailsImpl userDetails =(UserDetailsImpl)authentication.getPrincipal();

        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("roles",userDetails.getAuthorities());

        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, APPLICATION_SECRET_KEY)
                .compact();


    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(APPLICATION_SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(APPLICATION_SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }








}
