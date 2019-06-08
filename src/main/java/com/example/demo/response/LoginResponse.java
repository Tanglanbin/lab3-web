package com.example.demo.response;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getMsg() {
        return token;
    }
}
