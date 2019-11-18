package com.nivtech.petitecaisse.security;

import com.nivtech.petitecaisse.domain.entity.User;
import com.nivtech.petitecaisse.exception.ResourceNotFoundException;
import com.nivtech.petitecaisse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService
{

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
