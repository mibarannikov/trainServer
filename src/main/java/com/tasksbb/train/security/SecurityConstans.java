package com.tasksbb.train.security;

public class SecurityConstans {
    public static final String SIGN_UP_URL = "/api/auth/**";
    public static final String TRAIN_URL = "/api/train/**";
    public static final String STATION_URL = "/api/station/**";
    public static final String ADMIN_URL = "/api/admin/**";
    public static final String SECRET = "SecretKeyGenJWT";

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 600_000_000;
}
