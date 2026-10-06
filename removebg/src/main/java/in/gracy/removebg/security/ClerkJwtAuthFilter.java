package in.gracy.removebg.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ClerkJwtAuthFilter extends OncePerRequestFilter {

    @Value("${clerk.issuer}")
    private String clerkIssuer;

    private final ClerkJwksProvider jwksProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        /* Skip authentication for Clerk Webhook endpoints */
        if (request.getRequestURI().contains("/api/webhooks")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Authorization header missing or invalid"
            );
            return;
        }

        try {

            String token = authHeader.substring(7);

            /* JWT must have 3 parts */
            String[] chunks = token.split("\\.");

            if (chunks.length != 3) {
                throw new RuntimeException("Invalid JWT format");
            }

            /* Decode header to extract kid */
            String headerJson = new String(
                    Base64.getUrlDecoder().decode(chunks[0])
            );

            JsonNode headerNode = objectMapper.readTree(headerJson);

            String kid = headerNode.get("kid").asText();

            /* Get public key from Clerk JWKS */
            PublicKey publicKey = jwksProvider.getPublicKey(kid);

            /* Validate JWT */
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .setAllowedClockSkewSeconds(60)
                    .requireIssuer(clerkIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String clerkUserId = claims.getSubject();

            /* Set Authentication */
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            clerkUserId,
                            null,
                            Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_USER")
                            )
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Invalid JWT Token"
            );
        }
    }
}