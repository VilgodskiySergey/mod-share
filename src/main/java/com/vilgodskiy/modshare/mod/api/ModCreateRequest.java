package com.vilgodskiy.modshare.mod.api;

import com.vilgodskiy.modshare.mod.domain.Mod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Getter
@Setter
@ApiModel(value = "Мод (создание)")
public class ModCreateRequest {

    @ApiModelProperty(value = Mod.TITLE_FIELD, required = true)
    private String title;

    @ApiModelProperty(value = Mod.GOOGLE_DIVE_FILE_ID_FIELD, required = true)
    private String googleDriveFileId;

    @ApiModelProperty(value = Mod.ZIP_NAME_FIELD, required = true)
    private String zipName;

    @ApiModelProperty(value = Mod.EDITING_FILE_PATH_FIELD, required = true)
    private String editingFilePath;
}