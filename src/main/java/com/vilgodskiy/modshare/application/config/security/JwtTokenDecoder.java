package com.vilgodskiy.modshare.application.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vilgodskiy.modshare.application.config.security.domain.AccountJwtToken;
import com.vilgodskiy.modshare.application.exception.AuthApplicationException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.vilgodskiy.modshare.application.config.security.WebSecurityConfig.TOKEN_PREFIX;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Component
public class JwtTokenDecoder {

    @Value("${jwt.algorithm}")
    private String algorithm;

    @Value("${jwt.publicKey}")
    private String publicKey;

    @Value("${jwt.privateKey:#{null}}")
    private String privateKey;

    public AccountJwtToken decodeStringToken(String token) {
        DecodedJWT jwt;
        try {
            jwt = JWT.require(getAlgorithm())
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
        } catch (SignatureVerificationException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Неверный формат токена");
        } catch (AlgorithmMismatchException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Неверный алгоритм шифрования токена");
        } catch (TokenExpiredException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Токен истёк");
        } catch (JWTDecodeException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Неподдерживаемый формат токена");
        }
        return new AccountJwtToken(jwt);
    }

    @SneakyThrows
    public Algorithm getAlgorithm() {
        if ("HMAC512".equals(algorithm)) {
            return Algorithm.HMAC512(publicKey);
        } else if ("RSA256".equals(algorithm)) {
            byte[] privateKeyByte = Base64.getDecoder().decode(privateKey.getBytes());
            RSAPrivateKey privKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));

            byte[] publicKeyByte = Base64.getDecoder().decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyByte));
            return Algorithm.RSA256(pubKey, privKey);
        }
        throw new AuthApplicationException("Алгоритм не поддерживается");
    }
}
