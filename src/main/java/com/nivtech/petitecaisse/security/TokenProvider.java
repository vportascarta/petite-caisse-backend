package com.nivtech.petitecaisse.security;

import com.nivtech.petitecaisse.config.AppProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider
{

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties)
    {
        this.appProperties = appProperties;
    }

    public boolean validateApiToken(String apiToken)
    {
        return appProperties.getAuth().getAuthorizedApiToken().contains(apiToken);
    }

    public String createJwt(Authentication authentication)
    {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromJwt(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateJwt(String authToken)
    {
        try
        {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex)
        {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex)
        {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex)
        {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex)
        {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex)
        {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
