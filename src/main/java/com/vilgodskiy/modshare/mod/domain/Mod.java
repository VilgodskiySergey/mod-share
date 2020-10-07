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
     * Rating
     */
    @Column(name = "i_rating")
    private Integer rating;

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

    public Mod(String title, User owner) {
        this.title = title;
        this.owner = owner;
        this.tracingInfo = new TracingInfo(owner);
        Validators.TITLE
                .then(Validators.OWNER)
                .validate(this)
                .throwIfHasErrors();
    }

    /**
     * Update mod
     *
     * @param title    - new title
     * @param executor - who updated
     * @return - himself (chain-request)
     */
    public Mod update(String title, User executor) {
        this.title = title;
        this.tracingInfo.modified(executor);
        return this;
    }

    public enum Validators implements Validator<Mod> {
        OWNER {
            @Override
            public ValidationResult validate(Mod mod) {
                if (mod.getOwner() == null) {
                    return new ValidationResultImpl(EMPTY_OWNER);
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
                        .validateEmptyAndMaxSize(mod.getTitle(), Mod.TITLE_LENGTH, "Наименование");
            }
        };

        public static final String EMPTY_OWNER = "Не задан владелец мода";
        public static final String OWNER_INCORRECT_ROLE = "Мод может создать только пользователь с ролью - "
                + Role.MOD_DEVELOPER.getTitle();
    }
}