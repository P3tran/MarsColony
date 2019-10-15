package com.pefthymiou.mars.user;

import com.pefthymiou.mars.base.builders.AppUserBuilder;
import com.pefthymiou.mars.user.domain.actions.login.UserService;
import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import com.pefthymiou.mars.user.infrastructure.db.AppUserRepository;
import com.pefthymiou.mars.user.infrastructure.db.Role;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceShould {

    private static final String USERNAME = "bob";
    private static final String PASSWORD = "123";
    private static final Set<Role> ROLES = new HashSet<>(asList(new Role("ADMIN", "administrator"), new Role("COLONIST", "colonist")));
    private static final List<SimpleGrantedAuthority> AUTHORITIES = asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("COLONIST"));
    private static final AppUser APP_USER = AppUserBuilder.aUser().withUsername(USERNAME).withPassword(PASSWORD).withRoles(ROLES).build();
    private static final User USER = new User(USERNAME, PASSWORD, AUTHORITIES);

    private UserService userService;

    @Mock
    private AppUserRepository userRepository;

    @Before
    public void setup() {
        initMocks(this);
        userService = new UserService(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void throwUsernameNotFoundExceptionWhenUserCouldNotBeFoundByUsername() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(null);

        userService.loadUserByUsername(USERNAME);
    }

    @Test
    public void returnUserWithLoadedUserData() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(APP_USER);

        User loadedUser = (User) userService.loadUserByUsername(USERNAME);

        assertThat(loadedUser).isEqualToComparingFieldByFieldRecursively(USER);
    }
}
