    package com.viajes.viajesCompartidos.security;

    import io.jsonwebtoken.*;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;

    import java.security.Key;
    import java.util.Date;
    import com.viajes.viajesCompartidos.entities.User;

    @Component
    public class JwtUtil {
      private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Esto generará una clave de 512 bits
        private final int EXPIRATION_TIME = 86400000; // Tiempo de expiración en milisegundos (24 horas)

        public String generateToken(UserDetails user) {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractUsername(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        public boolean validateToken(String token, UserDetails userDetails) {
            final String email = extractUsername(token);
            return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
        public boolean validateToken(String token) {
            try {
                // Valida la firma del token
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return !isTokenExpired(token); // Verifica si el token está expirado
            } catch (JwtException e) {
                // Token no es válido si la firma no coincide o ha sido manipulado
                return false;
            }
        }


        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        }
    }
