package com.mattcom.demostoreapp.api.controller.security;

import com.mattcom.demostoreapp.user.StoreUserRepository;
import com.mattcom.demostoreapp.user.StoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JUnitUserDetailsService implements UserDetailsService {

    @Autowired
    private StoreUserRepository storeUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<StoreUser> storeUser = storeUserRepository.findByEmailIgnoreCase(username);
        if (storeUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(storeUser.get().getEmail(), storeUser.get().getPassword(), true, true, true, true, new ArrayList<>());
        return userDetails;
    }
}
