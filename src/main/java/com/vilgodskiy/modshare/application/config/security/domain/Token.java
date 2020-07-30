package com.vilgodskiy.modshare.application.config.security.domain;

import com.vilgodskiy.modshare.user.api.UserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZoneOffset;

@Getter
@AllArgsConstructor
public class Token {

    @ApiModelProperty(required = true)
    private String access_token;

    @ApiModelProperty(example = "1234567890123", required = true)
    private long accessTokenExpires;

    @ApiModelProperty(value = "DTO пользователя",
            required = true)
    private UserResponse account;

    public Token(TokenHolder tokenHolder, UserResponse account) {
        access_token = tokenHolder.getAccessToken();
        accessTokenExpires = tokenHolder.getAccessTokenExpires().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.account = account;
    }
}
