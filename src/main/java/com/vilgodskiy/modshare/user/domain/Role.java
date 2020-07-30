package com.vilgodskiy.modshare.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum Role {

    CONSUMER("Покупатель"),
    MOD_DEVELOPER("Разработчик модов"),
    ADMINISTRATOR("Администратор");

    private final String title;

    public String getName() {
        return name();
    }

}
