package com.pefthymiou.mars.user.domain.actions.login;

import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import com.pefthymiou.mars.user.infrastructure.db.AppUserRepository;
import com.pefthymiou.mars.user.infrastructure.db.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class UserService implements UserDetailsService {

    private AppUserRepository userRepository;

    @Autowired
    public UserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null)
            throw new UsernameNotFoundException(username);

        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                authoritiesFrom(appUser.getRoles())
        );
    }

    private List<SimpleGrantedAuthority> authoritiesFrom(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(toList());
    }
}
