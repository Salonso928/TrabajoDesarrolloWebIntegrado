package sabor.tradicion.restaurantesabortradicion.Servicios;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sabor.tradicion.restaurantesabortradicion.DTOs.AuthDTO.LoginResponseDTO;
import sabor.tradicion.restaurantesabortradicion.Entidades.Usuario;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //Esto es de momento, solo para probar mis datos
    private final String SECRET_KEY_STRING = "SABORYTRADICION123456CLAVESECRETA";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        Usuario usuario = (Usuario) userDetails;
        Map<String, Object> extraClaims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        extraClaims.put("id",usuario.getIdUsuario());
        extraClaims.put("roles", roles);


        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas va a durar el token
                .signWith(getSigningKey())
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
