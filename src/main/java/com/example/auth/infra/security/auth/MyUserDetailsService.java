package com.example.auth.infra.security.auth;

import com.example.auth.domain.exceptions.auth.AuthException;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.entities.auth.MyUserDetails;
import com.example.auth.infra.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private final AuthException authException;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow((authException::exceptionInvalidUsername));

        return new MyUserDetails(user);
    }
}

