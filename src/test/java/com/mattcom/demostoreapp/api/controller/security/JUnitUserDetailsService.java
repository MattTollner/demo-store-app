package com.mattcom.demostoreapp.api.controller.security;

import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.StoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JUnitUserDetailsService implements UserDetailsService {

    @Autowired
    private StoreUserRepository storeUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<StoreUser> storeUser = storeUserRepository.getUserAndRolesByEmailIgnoreCase(username);
        if (storeUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return storeUser.get();
    }
}
