package com.example.Sprachraume.security.sec_service;


import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class TokenService {

    private  SecretKey accessSecretKey;
    private  SecretKey refreshSecretKey;



}
