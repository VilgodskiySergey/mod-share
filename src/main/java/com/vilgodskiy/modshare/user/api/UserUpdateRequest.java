package com.vilgodskiy.modshare.user.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
@Getter
@Setter
@ApiModel(value = "DTO (входящее) редактирование User")
public class UserUpdateRequest {

    @ApiModelProperty(value = "Имя", required = true)
    private String firstName;

    @ApiModelProperty(value = "Фамилия", required = true)
    private String lastName;

    @ApiModelProperty(value = "Отчество")
    private String middleName;

    @ApiModelProperty(value = "Телефон")
    private String phone;

}