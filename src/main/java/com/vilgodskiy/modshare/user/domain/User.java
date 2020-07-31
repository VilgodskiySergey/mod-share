package com.vilgodskiy.modshare.user.domain;

import com.elementsoft.common.Validator;
import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.util.ValidationUtils;
import com.vilgodskiy.modshare.util.Savable;
import com.vilgodskiy.modshare.util.TracingInfo;
import com.vilgodskiy.modshare.util.Validable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Platform user
 */
@Entity
@Table(name = "mds_user")
@Getter
@NoArgsConstructor
public class User implements Validable<User>, Savable<User> {

    public static final String PATH = "user";

    public static final int FIRST_NAME_LENGTH = 32;
    public static final int LAST_NAME_LENGTH = 32;
    public static final int MIDDLE_NAME_LENGTH = 32;
    public static final int EMAIL_LENGTH = 254;
    public static final int PHONE_MAX_LENGTH = 16;
    public static final int LOGIN_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 32;

    private static final Pattern LOGIN_PATTERN = Pattern.compile("[a-zA-Z\\d]+");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uuid", columnDefinition = "UUID")
    private UUID id;

    /**
     * Locked flag
     */
    @Column(name = "b_locked", nullable = false)
    private boolean locked;

    /**
     * First name
     */
    @Column(name = "s_first_name", length = FIRST_NAME_LENGTH)
    private String firstName;

    /**
     * Last name
     */
    @Column(name = "s_last_name", length = LAST_NAME_LENGTH)
    private String lastName;

    /**
     * Middle name
     */
    @Column(name = "s_middle_name", length = MIDDLE_NAME_LENGTH)
    private String middleName;

    /**
     * Email
     */
    @Column(name = "s_email", length = EMAIL_LENGTH, nullable = false, unique = true)
    private String email;

    /**
     * Phone number
     */
    @Column(name = "s_phone", length = PHONE_MAX_LENGTH)
    private String phone;

    /**
     * Username (login)
     */
    @Column(name = "s_login", length = LOGIN_MAX_LENGTH, nullable = false, unique = true)
    private String login;

    /**
     * Hash from password
     */
    @Column(name = "s_password_hash", nullable = false)
    private String passwordHash;

    /**
     * Role
     */
    @Column(name = "s_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Tracing info
     */
    @Embedded
    private TracingInfo tracingInfo;

    public User(String firstName, String lastName, String middleName, String email, String phone, String login,
                String passwordHash, Role role, User executor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = StringUtils.toRootLowerCase(email);
        this.phone = phone;
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
        this.tracingInfo = new TracingInfo(executor);
        Validators.LOGIN
                .then(Validators.NAMES)
                .then(Validators.EMAIL)
                .then(Validators.ROLE)
                .then(Validators.PHONE)
                .validate(this)
                .throwIfHasErrors();
    }

    public User update(String firstName, String lastName, String middleName, String phone, User executor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.tracingInfo.modified(executor);
        Validators.NAMES
                .then(Validators.PHONE)
                .validate(this)
                .throwIfHasErrors();
        return this;
    }

    public enum Validators implements Validator<User> {
        EMAIL {
            @Override
            public ValidationResult validate(User user) {
                return ValidationUtils.validateEmptyAndMaxSize(user.getEmail(), User.EMAIL_LENGTH, "Email")
                        .addResult(ValidationUtils.validatePattern(user.getEmail(), EMAIL_PATTERN, INCORRECT_EMAIL));
            }
        },
        LOGIN {
            @Override
            public ValidationResult validate(User user) {
                return ValidationUtils
                        .validateEmptyAndMaxSize(user.getLogin(), User.LOGIN_MAX_LENGTH, "Логин")
                        .addResult(ValidationUtils.validatePattern(user.getLogin(), LOGIN_PATTERN, INCORRECT_LOGIN));
            }
        },
        ROLE {
            @Override
            public ValidationResult validate(User user) {
                return ValidationUtils.validateNullable(user.getRole(), "Роль");
            }
        },
        NAMES {
            @Override
            public ValidationResult validate(User user) {
                return ValidationUtils.validateMaxSize(user.getFirstName(), FIRST_NAME_LENGTH, "Имя")
                        .addResult(ValidationUtils.validateMaxSize(
                                user.getLastName(), LAST_NAME_LENGTH, "Фамилия"))
                        .addResult(ValidationUtils.validateMaxSize(
                                user.getMiddleName(), MIDDLE_NAME_LENGTH, "Отчество"));
            }
        },
        PHONE {
            @Override
            public ValidationResult validate(User user) {
                return ValidationUtils.validateMaxSize(user.getPhone(), PHONE_MAX_LENGTH, "Телефон");
            }
        };

        public static final String INCORRECT_EMAIL = "Некоректный Email";
        public static final String INCORRECT_LOGIN = "Некоректный логин";
    }
}