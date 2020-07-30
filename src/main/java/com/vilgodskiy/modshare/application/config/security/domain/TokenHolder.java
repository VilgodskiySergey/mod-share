package com.vilgodskiy.modshare.application.config.security.domain;

import com.vilgodskiy.modshare.util.Savable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "mbs_token_holder")
@NoArgsConstructor
@AllArgsConstructor
public class TokenHolder implements Savable<TokenHolder> {

    @Id
    @Column(name = "s_access_token", length = 2048, nullable = false)
    private String accessToken;

    @Column(name = "s_access_token_expires", nullable = false)
    private LocalDateTime accessTokenExpires;

    @Column(name = "uuid_session", nullable = false)
    private String sessionId;

    @Column(name = "uuid_account", nullable = false)
    private UUID accountId;

    @Column(name = "b_revoked", nullable = false)
    private boolean revoked;
}
