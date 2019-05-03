package io.microshow.rxretrofit.encrypt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

/**
 * JWT加密
 */
public class JWTFactory {

    /**
     * 生成秘钥
     */
    public static String getSecret(long currentTime, String jwtIss, String jwtSecret) {
        String token;
        if (currentTime == 0) {
            currentTime = System.currentTimeMillis();
        }
        Date expDate = new Date(currentTime + 3 * 60 * 1000); // 过期时间 3分钟
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("vvic_android")
                .issuer(jwtIss)
                .expirationTime(expDate)
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            JWSSigner signer = new MACSigner(jwtSecret);
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            token = "";
        }
        return token;
    }

}
