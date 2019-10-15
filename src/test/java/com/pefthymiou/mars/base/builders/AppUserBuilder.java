package com.pefthymiou.mars.base.builders;

import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import com.pefthymiou.mars.user.infrastructure.db.Role;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

public class AppUserBuilder {

    private String username = "bob";
    private String password = "123";
    private String email = "someone@example.com";
    private String timezone = "UTC";
    private Set<Role> roles = new HashSet<>(singletonList(new Role("ADMIN", "administrator")));

    public static AppUserBuilder aUser() {
        return new AppUserBuilder();
    }

    public AppUserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public AppUserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public AppUserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUserBuilder withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public AppUserBuilder withRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public AppUser build() {
        return new AppUser(username, password, email, timezone, roles);
    }
}
