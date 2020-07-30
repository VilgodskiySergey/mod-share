package com.vilgodskiy.modshare.init.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
public interface InitScriptRepository extends JpaRepository<InitScript, String> {

    Optional<InitScript> findByName(String simpleName);

}
