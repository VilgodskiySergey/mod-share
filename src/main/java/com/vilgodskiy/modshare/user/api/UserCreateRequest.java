package com.vilgodskiy.modshare.user.api;

import com.vilgodskiy.modshare.user.domain.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
@Getter
@Setter
@ApiModel(value = "DTO (входящее) User")
public class UserCreateRequest {

    @ApiModelProperty(value = "Имя", required = true)
    private String firstName;

    @ApiModelProperty(value = "Фамилия", required = true)
    private String lastName;

    @ApiModelProperty(value = "Отчество")
    private String middleName;

    @ApiModelProperty(value = "Email", required = true)
    private String email;

    @ApiModelProperty(value = "Телефон")
    private String phone;

    @ApiModelProperty(value = "Логин", required = true)
    private String username;

    @ApiModelProperty(value = "Пароль", required = true)
    private String password;

    @ApiModelProperty(value = "Роль", required = true)
    private Role role;

}