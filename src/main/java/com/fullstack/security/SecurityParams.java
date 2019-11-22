package com.fullstack.security;

public interface   SecurityParams {
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final String SECRET = "forthesakeoftest";
    public static final long EXPIRATION = 10*24*3600*1000;
    public static final String TOKEN_PREFIX = "Bearer ";

}
