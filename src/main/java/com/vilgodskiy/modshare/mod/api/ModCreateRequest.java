package com.vilgodskiy.modshare.mod.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Getter
@Setter
@ApiModel(value = "Мод (создание)")
public class ModCreateRequest {

    @ApiModelProperty(value = "Наименование", required = true)
    private String title;
}