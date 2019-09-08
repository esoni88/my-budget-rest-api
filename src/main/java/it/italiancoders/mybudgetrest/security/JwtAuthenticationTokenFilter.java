package it.italiancoders.mybudgetrest.security;

import io.jsonwebtoken.ExpiredJwtException;
import it.italiancoders.mybudgetrest.exception.security.ExpiredTokenException;
import it.italiancoders.mybudgetrest.service.security.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Value("${jwt.accessToken.name}")
    private String tokenHeader;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(this.tokenHeader);

        UserDetails userDetails = null;

        if(authToken != null){
            try {
               userDetails = jwtTokenManager.getUserDetails(authToken).orElse(null);
            } catch (ExpiredJwtException e) {

                request.setAttribute("isExpired", true);
                chain.doFilter(request, response);
                return;
            }

        }

        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Ricostruisco l userdetails con i dati contenuti nel token


            // controllo integrita' token
            boolean validToken = true;
            try {
                jwtTokenManager.validateAccessToken(authToken, userDetails);
            } catch (ExpiredTokenException e) {
                validToken = false;
                throw e;
            } catch (Exception e) {
                logger.error("Invalid token [{}] " , e);
                validToken = false;
            }

            if (validToken) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        chain.doFilter(request, response);
    }
}
