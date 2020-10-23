package com.vilgodskiy.modshare.mod.api;

import com.vilgodskiy.modshare.mod.domain.Mod;
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

    @ApiModelProperty(value = Mod.TITLE_FIELD)
    private String title;

    @ApiModelProperty(value = Mod.OWNER)
    private UserResponse owner;

    @ApiModelProperty(value = "Рейтинг")
    private Integer rating;

    @ApiModelProperty(value = Mod.GOOGLE_DIVE_FILE_ID_FIELD)
    private String googleDriveFileId;

    @ApiModelProperty(value = Mod.ZIP_NAME_FIELD)
    private String fileName;

    @ApiModelProperty(value = Mod.EDITING_FILE_PATH_FIELD)
    private String editingFilePath;
}