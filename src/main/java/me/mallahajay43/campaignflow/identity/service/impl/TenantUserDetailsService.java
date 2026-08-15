package me.mallahajay43.campaignflow.identity.service.impl;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", email));
    }
}
