package ru.ntwz.feedify.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.UserRepository;
import ru.ntwz.feedify.service.CustomUserDetailsService;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }

    public UserDetails loadUserByUsername(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }

        return user;
    }
}
