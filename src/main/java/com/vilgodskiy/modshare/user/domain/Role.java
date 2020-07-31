package com.vilgodskiy.modshare.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {

    CONSUMER("Покупатель"),
    MOD_DEVELOPER("Разработчик модов"),
    ADMINISTRATOR("Администратор");

    public static final String CONSUMER_ROLE = "CONSUMER";
    public static final String MOD_DEVELOPER_ROLE = "MOD_DEVELOPER";
    public static final String ADMINISTRATOR_ROLE = "ADMINISTRATOR";

    private final String title;

    public String getName() {
        return name();
    }

    @JsonCreator
    public static Role fromNode(@JsonProperty("name") String name) {
        return name != null ? valueOf(name) : null;
    }
}
