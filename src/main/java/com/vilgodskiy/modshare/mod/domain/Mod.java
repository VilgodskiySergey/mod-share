package com.vilgodskiy.modshare.mod.domain;

import com.elementsoft.common.Validator;
import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.result.ValidationResultImpl;
import com.elementsoft.common.util.ValidationUtils;
import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.Savable;
import com.vilgodskiy.modshare.util.TracingInfo;
import com.vilgodskiy.modshare.util.Validable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Entity
@Table(name = "mds_mod")
@Getter
@NoArgsConstructor
public class Mod implements Validable<Mod>, Savable<Mod> {

    public static final String PATH = "mod";
    public static final String ENTITY_NAME = "Мод";
    public static final int TITLE_LENGTH = 128;
    public static final int ZIP_NAME_LENGTH = 128;

    public static final String TITLE_FIELD = "Наименование";
    public static final String GOOGLE_DIVE_FILE_ID_FIELD = "ID на Google drive";
    public static final String ZIP_NAME_FIELD = "Имя zip-архива";
    public static final String EDITING_FILE_PATH_FIELD = "Путь до редактируемого файла в архиве";
    public static final String OWNER = "Владелец мода";
    public static final String FREE = "Бесплатная";

    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uuid", columnDefinition = "UUID")
    private UUID id;

    /**
     * Title
     */
    @Column(name = "s_title", nullable = false, length = TITLE_LENGTH)
    private String title;

    /**
     * File id on google drive
     */
    @Column(name = "s_google_drive_file_id", nullable = false)
    private String googleDriveFileId;

    /**
     * File Name (zip)
     */
    @Column(name = "s_zip_name", length = ZIP_NAME_LENGTH, nullable = false)
    private String zipName;

    /**
     * Path to editing file in zip
     */
    @Column(name = "s_editing_file_path", nullable = false)
    private String editingFilePath;

    /**
     * Rating
     */
    @Column(name = "i_rating")
    private Integer rating;

    /**
     * Free
     */
    @Column(name = "b_free", nullable = false)
    private boolean free;

    /**
     * Owner (MOD_DEVELOPER)
     */
    @ManyToOne
    @JoinColumn(name = "uuid_owner", nullable = false, foreignKey = @ForeignKey(name = "FK_mod__owner"))
    private User owner;

    /**
     * Tracing info
     */
    @Embedded
    private TracingInfo tracingInfo;

    public Mod(String title, String googleDriveFileId, String zipName, String editingFilePath, boolean free, User owner) {
        this.title = title;
        this.owner = owner;
        this.googleDriveFileId = googleDriveFileId;
        this.zipName = zipName;
        this.editingFilePath = editingFilePath;
        this.free = free;
        this.tracingInfo = new TracingInfo(owner);
        Validators.TITLE
                .then(Validators.GOOGLE_DRIVE_FILE_ID)
                .then(Validators.FILE_NAME)
                .then(Validators.EDITING_FILE_PATH)
                .then(Validators.OWNER)
                .validate(this)
                .throwIfHasErrors();
    }

    /**
     * Update mod
     *
     * @param title    - new title
     * @param free - is it free?
     * @param executor - who updated
     * @return - himself (chain-request)
     */
    public Mod update(String title, String googleDriveFileId, String zipName, String editingFilePath, boolean free,
                      User executor) {
        this.title = title;
        this.googleDriveFileId = googleDriveFileId;
        this.zipName = zipName;
        this.editingFilePath = editingFilePath;
        this.free = free;
        this.tracingInfo.modified(executor);
        Validators.TITLE
                .then(Validators.GOOGLE_DRIVE_FILE_ID)
                .then(Validators.FILE_NAME)
                .then(Validators.EDITING_FILE_PATH)
                .then(Validators.OWNER)
                .validate(this)
                .throwIfHasErrors();
        return this;
    }

    public enum Validators implements Validator<Mod> {
        OWNER {
            @Override
            public ValidationResult validate(Mod mod) {
                if (mod.getOwner() == null) {
                    return ValidationUtils.validateNullable(mod.getOwner(), Mod.OWNER);
                } else {
                    if (mod.getOwner().getRole() != Role.MOD_DEVELOPER) {
                        return new ValidationResultImpl(OWNER_INCORRECT_ROLE);
                    }
                }
                return ValidationResult.EMPTY;
            }
        },
        TITLE {
            @Override
            public ValidationResult validate(Mod mod) {
                return ValidationUtils
                        .validateEmptyAndMaxSize(mod.getTitle(), Mod.TITLE_LENGTH, TITLE_FIELD);
            }
        },
        GOOGLE_DRIVE_FILE_ID {
            @Override
            public ValidationResult validate(Mod mod) {
                return ValidationUtils
                        .validateEmpty(mod.getGoogleDriveFileId(), GOOGLE_DIVE_FILE_ID_FIELD);
            }
        },
        FILE_NAME {
            @Override
            public ValidationResult validate(Mod mod) {
                return ValidationUtils
                        .validateEmptyAndMaxSize(mod.getZipName(), Mod.ZIP_NAME_LENGTH, ZIP_NAME_FIELD);
            }
        },
        EDITING_FILE_PATH {
            @Override
            public ValidationResult validate(Mod mod) {
                return ValidationUtils
                        .validateEmpty(mod.getEditingFilePath(), EDITING_FILE_PATH_FIELD);
            }
        };

        public static final String OWNER_INCORRECT_ROLE = "Мод может создать только пользователь с ролью - "
                + Role.MOD_DEVELOPER.getTitle();
    }
}