package com.mattcom.demostoreapp.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.mattcom.demostoreapp.auth.JWTService;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.StoreUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final StoreUserRepository storeUserRepository;

    public JWTRequestFilter(JWTService jwtService, StoreUserRepository storeUserRepository) {
        this.jwtService = jwtService;
        this.storeUserRepository = storeUserRepository;
    }

    //Filter chain allows for multiple filters to be done in order
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String jwtToken = null;

        //If it starts with bearer then we know its valid
        if ((tokenHeader != null && tokenHeader.startsWith("Bearer "))) {
           // String token = jwtToken != null ? jwtToken : tokenHeader.substring(7);
            String token =tokenHeader.substring(7);
            try {
                //Decode the token and get the email
                String email = jwtService.getEmail(token);
                Optional<StoreUser> userOptional = storeUserRepository.getUserAndRolesByEmail(email);
                if (userOptional.isPresent()) {
                    StoreUser user = userOptional.get();
                    if (user.isEmailVerified()) {
                        //Build up auth object
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        //Informs spring security about the user
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //Stores the auth into the context
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (JWTDecodeException exception) {

            }
        }

        //Passes the data to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
