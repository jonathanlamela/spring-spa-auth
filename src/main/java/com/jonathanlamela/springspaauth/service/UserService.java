package com.jonathanlamela.springspaauth.service;

import org.springframework.stereotype.Component;

@Component("userSComponent")
public class UserService {

    public String getServiceVersion() {
        return "0.0.1";
    }

}
