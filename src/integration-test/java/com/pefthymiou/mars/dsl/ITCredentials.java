package com.pefthymiou.mars.dsl;

public class ITCredentials {

    private final String username;
    private final String password;

    public ITCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
