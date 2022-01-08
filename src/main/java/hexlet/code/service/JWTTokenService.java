package hexlet.code.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hexlet.code.service.interfaces.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;

@Service
public class JWTTokenService implements TokenService, Clock {
    private static final int ONE_MILLIS = 1000;
    @Value("${jwt.secret:secret}")
    private String secretKey;
    @Value("${jwt.issuer:issuer}")
    private String issuer;
    @Value("${jwt.expiration-sec:86400}")
    private long expirationSec;
    @Value("${jwt.clock-skew-sec:300}")
    private long clockSkewSec;

    @Override
    public final String expiring(Map<String, Object> attributes) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compressWith(new GzipCompressionCodec())
                .setClaims(this.getClaims(attributes))
                .compact();
    }

    @Override
    public final Map<String, Object> verify(String token) {
        return Jwts.parser()
                .requireIssuer(this.issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(this.clockSkewSec)
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public final Date now() {
        return new Date();
    }

    private Claims getClaims(Map<String, Object> attributes) {
        Claims claims = Jwts.claims();
        claims.setIssuer(this.issuer);
        claims.setIssuedAt(this.now());
        claims.setExpiration(new Date(System.currentTimeMillis() + this.expirationSec + ONE_MILLIS));
        claims.putAll(attributes);
        return claims;
    }
}
