package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.auth.api.services.CryptoUtilService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;

public class CryptoUtilServiceImpl implements CryptoUtilService {

    private static final Logger logger = LogManager.getLogger(CryptoUtilServiceImpl.class);

    @Override
    public String hashToken(String token) {
        return "";
    }

    @Override
    public SecretKey getAccessTokenSecretKey() {
        return null;
    }

    @Override
    public SecretKey getRefreshTokenKey() {
        return null;
    }

    @Override
    public long getAccessTokenExpirationInMs() {
        return 0;
    }

    @Override
    public long getRefreshTokenExpirationInMs() {
        return 0;
    }
}
