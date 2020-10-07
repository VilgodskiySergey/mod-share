package com.vilgodskiy.modshare.mod.api;

import com.vilgodskiy.modshare.user.api.UserResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "DTO Мод")
public class ModResponse {

    @ApiModelProperty(value = "ID")
    private UUID id;

    @ApiModelProperty(value = "Наименование")
    private String title;

    @ApiModelProperty(value = "Владелец")
    private UserResponse owner;

    @ApiModelProperty(value = "Рейтинг")
    private Integer rating;
}