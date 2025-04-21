package com.carlospi.crow.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "61a4d789a77a2c1737eb04235108dd54293d5dd47409e0c42607c2f2f699e9c004e0290ea5e908ad98593cb2b47ec480fa3f11b12230f65a960cf6c08cee0aee68ccaff6ace8b6665242e3d2fdee972ec5260cc9d65db33c1b96a7f4fa849db769932f827c4c8a12c74269478da7ff2e41e86f5e17db4eb2adeb45052ac65254df3ce973afcd860fcf4b3ea09a5582e7beacb71dcec04b1be3cb4438ecfad4c95c40d5879aac67366e0081366994d7c7b2bab5db630758d75c0723eb6841045e86aa37f5068006baf0db5a995d0f35e3a022b11adc15235538dbc2221eb79f773cfc3cc5aff70f134ebdf761a7cb5d8c2f493b7fb423cc57d2d65764efd5e003";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails); //Genera el token sin claims adicionales
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)                                               //Agrega los claims adicionales
                .setSubject(userDetails.getUsername())                                //Agrega el nombre de usuario
                .setIssuedAt(new Date(System.currentTimeMillis()))                    //Agrega la fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) //Agrega la fecha de expiración (1 día)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)                   //Firma el token con la clave secreta
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()                 //Permite configurar cómo se leerá/verificará el token.
                .setSigningKey(getSignInKey())   //Usa clave secreta
                .build()                         //Construye el parser JWT
                .parseClaimsJws(token)           //Parsea y valida el token JWT
                .getBody();                      //Obtiene los claims (contenido del token)
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
