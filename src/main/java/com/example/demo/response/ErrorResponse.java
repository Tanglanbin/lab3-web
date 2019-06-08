package com.example.demo.response;

public class ErrorResponse {
    private String errMsg;

   public  ErrorResponse(String msg) {
        this.errMsg = msg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
