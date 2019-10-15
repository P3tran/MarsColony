package com.pefthymiou.mars.security.configuration;

public class SecurityParams {

    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String SECRET = "AuthenticatorTokenForJWTSGeneration";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLES_KEY = "roles";
}
