package com.pefthymiou.mars.dsl;

public class Data {

    public static ITCredentials CREDENTIALS = new ITCredentials("bob", "123");

    public static ITCredentials BAD_CREDENTIALS = new ITCredentials("bob", "badPassword");
}
