package example.qlks.utils;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JWTUtils {


    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; //for 7 days

    private final SecretKey Key;
    @Value("${jwt_secret}")
    private String jwt_secret;

    public JWTUtils() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");

    }

    public String generateToken(UserDetails userDetails) {
        Date today = new Date();
        // tao token
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(today)
                .setExpiration(new Date(today.getTime()+EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS384,jwt_secret)
                .compact();
    }

//    public String extractUsername(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
//        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
//    }

//    public boolean isValidToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaims(token, Claims::getExpiration).before(new Date());
//    }
//  check token
public boolean validateAccessToken(String token){
    try {
        Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token);
        return true;
    }catch (ExpiredJwtException ex){
        log.error("Chuoi jwt token het hieu luc");
    }catch (UnsupportedJwtException ex){
        log.error("API server khong ho tro jwt");
    }catch (MalformedJwtException ex){
        log.error("Chuoi jwt khong dung dinh dang");
    }catch (SignatureException ex){
        log.error("Chuoi jwt ma hoa khong dung");
    }catch (IllegalArgumentException ex){
        log.error("Tham so truyen toi khong ton tai");
    }
    return false;
}

    // lay username tu chuoi token
    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token).getBody().getSubject();
    }
}
