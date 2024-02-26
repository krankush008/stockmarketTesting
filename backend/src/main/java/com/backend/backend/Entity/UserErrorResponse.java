package com.backend.backend.Entity;

public class UserErrorResponse {
    private long id;
    private String errorMsg;

    public UserErrorResponse() {
        // Default constructor for serialization
    }

    public UserErrorResponse(long id, String errorMsg) {
        this.id = id;
        this.errorMsg = errorMsg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
