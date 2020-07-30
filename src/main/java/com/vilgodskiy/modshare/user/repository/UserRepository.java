package com.vilgodskiy.modshare.user.repository;

import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.GetOrThrowRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>,
        GetOrThrowRepository<User, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByLoginIgnoreCase(String login);

}