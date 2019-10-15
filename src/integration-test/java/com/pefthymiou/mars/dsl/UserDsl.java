package com.pefthymiou.mars.dsl;

import com.pefthymiou.mars.user.infrastructure.db.Role;
import java.util.HashSet;
import java.util.Set;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class UserDsl {

    public static class ITUser {

        private long id;
        private String username;
        private String password;
        private String email;
        private String timezone;
        private Set<Role> roles;

        public ITUser(long id, String username, String password, String email, String timezone, Set<Role> roles) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.email = email;
            this.timezone = timezone;
            this.roles = roles;
        }

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getTimezone() {
            return timezone;
        }

        public Set<Role> getRoles() {
            return roles;
        }

        @Override
        public boolean equals(Object other) {
            return reflectionEquals(this, other);
        }

        @Override
        public int hashCode() {
            return reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return reflectionToString(this, MULTI_LINE_STYLE);
        }
    }

    public static class ITUserBuilder {

        private long id = 1;
        private String username = "bob";
        private String password = "123";
        private String email = "someone@example.com";
        private String timezone = "UTC";
        private Set<Role> roles = new HashSet<>(singletonList(new Role("ADMIN", "administrator")));

        public static ITUserBuilder aUser() {
            return new ITUserBuilder();
        }

        public ITUserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public ITUserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public ITUserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ITUserBuilder withTimezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public ITUserBuilder withRoles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public ITUser build() {
            return new ITUser(id, username, password, email, timezone, roles);
        }
    }
}
