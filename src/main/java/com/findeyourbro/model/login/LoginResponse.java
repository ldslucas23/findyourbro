package com.findeyourbro.model.login;

public class LoginResponse {

    private String userName;
    private Long userId;
    private String token;
    
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
       
}
