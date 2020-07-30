package com.vilgodskiy.modshare.user.api;

import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.util.TracingInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "DTO Пользователь")
public class UserResponse {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private UUID id;

    /**
     * Locked flag
     */
    @ApiModelProperty(value = "Заблокированный")
    private boolean locked;

    /**
     * First name
     */
    @ApiModelProperty(value = "Имя")
    private String firstName;

    /**
     * Last name
     */
    @ApiModelProperty(value = "Фамилия")
    private String lastName;

    /**
     * Middle name
     */
    @ApiModelProperty(value = "Отчество")
    private String middleName;

    /**
     * Email
     */
    @ApiModelProperty(value = "Электронная почта")
    private String email;

    /**
     * Phone number
     */
    @ApiModelProperty(value = "Телефон")
    private String phone;

    /**
     * Username (login)
     */
    @ApiModelProperty(value = "Логин")
    private String login;

    /**
     * Role
     */
    @ApiModelProperty(value = "Роль")
    private Role role;

    /**
     * Tracing info
     */
    @ApiModelProperty(value = "Историчность")
    private TracingInfo tracingInfo;

}