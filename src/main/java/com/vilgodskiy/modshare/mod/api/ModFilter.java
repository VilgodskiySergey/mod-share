package com.vilgodskiy.modshare.mod.api;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Setter
@Getter
class ModFilter {

    @ApiParam("Поисковая строка")
    private String searchString;

}
