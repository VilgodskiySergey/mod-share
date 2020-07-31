package com.vilgodskiy.modshare.user;

import com.elementsoft.common.exception.ValidationException;
import com.vilgodskiy.modshare.application.exception.NotFoundObjectException;
import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.domain.User_;
import com.vilgodskiy.modshare.user.service.UserUniqueStringFieldValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.vilgodskiy.modshare.util.ServiceUtil.*;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<Role> roles = List.of(Role.values());

    @Test
    void createRandom() {
        Assertions.assertDoesNotThrow(UserServiceTest::createRandomUser);
    }


    @Test
    void create() {
        String firstName = randomString(User.FIRST_NAME_LENGTH);
        String lastName = randomString(User.LAST_NAME_LENGTH);
        String middleName = randomString(User.MIDDLE_NAME_LENGTH);
        String email = (randomString(10) + "@" + randomString(5) + ".com").toLowerCase();
        String phone = randomString(User.PHONE_MAX_LENGTH, false, true);
        String login = randomString(User.LOGIN_MAX_LENGTH);
        String password = randomString(User.PASSWORD_MAX_LENGTH);
        Role role = getRandomRole();
        User user = Assertions.assertDoesNotThrow(() -> userService.create(firstName, lastName, middleName, email,
                phone, login, password, role, getAdmin()));
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertEquals(middleName, user.getMiddleName());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(phone, user.getPhone());
        Assertions.assertEquals(login, user.getLogin());
        Assertions.assertEquals(role, user.getRole());
    }

    @Test
    void createWithIncorrectEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> createUser(null));
        Assertions.assertEquals(1, exception.getErrors().size());
        String emptyEmail = "";
        exception = Assertions.assertThrows(ValidationException.class, () -> createUser(emptyEmail));
        Assertions.assertEquals(1, exception.getErrors().size());
        String tooLongEmail = randomString(User.EMAIL_LENGTH) + "@gmal.com";
        exception = Assertions.assertThrows(ValidationException.class, () -> createUser(tooLongEmail));
        Assertions.assertEquals(1, exception.getErrors().size());
        String incorrectEmail = "incorrectEmail";
        exception = Assertions.assertThrows(ValidationException.class, () ->
                createUser(incorrectEmail));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(User.Validators.INCORRECT_EMAIL, exception.getErrors().get(0));
    }

    @Test
    void createWithDuplicateEmail() {
        String email = randomString() + "@mds.ru";
        createUser(email);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> createUser(email));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(String.format(UserUniqueStringFieldValidator.IS_DUPLICATE, User_.EMAIL, email),
                exception.getErrors().get(0));
    }

    @Test
    void createWithIncorrectLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> createUser(null, getRandomRole()));
        Assertions.assertEquals(1, exception.getErrors().size());
        String emptyLogin = "";
        exception = Assertions.assertThrows(ValidationException.class, () -> createUser(emptyLogin, getRandomRole()));
        Assertions.assertEquals(1, exception.getErrors().size());
        String tooLongLogin = randomString(User.LOGIN_MAX_LENGTH + 1);
        exception = Assertions.assertThrows(ValidationException.class, () -> createUser(tooLongLogin, getRandomRole()));
        Assertions.assertEquals(1, exception.getErrors().size());
        String incorrectLogin = "НекорректныйЛогин";
        exception = Assertions.assertThrows(ValidationException.class, () -> createUser(incorrectLogin, getRandomRole()));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(User.Validators.INCORRECT_LOGIN, exception.getErrors().get(0));
    }

    @Test
    void createWithDuplicateLogin() {
        String login = randomString();
        createUser(login, getRandomRole());
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> createUser(login, getRandomRole()));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(String.format(UserUniqueStringFieldValidator.IS_DUPLICATE, "Логин", login),
                exception.getErrors().get(0));
    }

    @Test
    void createWithoutRole() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> createUser(randomString(), null));
        Assertions.assertEquals(1, exception.getErrors().size());
    }

    @Test
    void createWithTooLongNames() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(randomString(User.FIRST_NAME_LENGTH + 1), null, null,
                        randomString() + "@gmail.com", null, randomString(), randomString(),
                        getRandomRole(), getAdmin()));
        Assertions.assertEquals(1, exception.getErrors().size());
        exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(null, randomString(User.LAST_NAME_LENGTH + 1), null,
                        randomString() + "@gmail.com", null, randomString(), randomString(),
                        getRandomRole(), getAdmin()));
        Assertions.assertEquals(1, exception.getErrors().size());
        exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(null, null, randomString(User.MIDDLE_NAME_LENGTH + 1),
                        randomString() + "@gmail.com", null, randomString(), randomString(),
                        getRandomRole(), getAdmin()));
        Assertions.assertEquals(1, exception.getErrors().size());
    }

    @Test
    void createWithTooLongPhone() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.create(null, null, null,
                        randomString() + "@gmail.com", randomString(User.PHONE_MAX_LENGTH + 1),
                        randomString(), randomString(), getRandomRole(), getAdmin()));
        Assertions.assertEquals(1, exception.getErrors().size());
    }

    @Test
    void update() {
        String firstName = randomString(User.FIRST_NAME_LENGTH);
        String lastName = randomString(User.LAST_NAME_LENGTH);
        String middleName = randomString(User.MIDDLE_NAME_LENGTH);
        String phone = randomString(User.PHONE_MAX_LENGTH, false, true);
        User user = Assertions.assertDoesNotThrow(() -> userService.update(createRandomUser().getId(), firstName,
                lastName, middleName, phone, getAdmin()));
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertEquals(middleName, user.getMiddleName());
        Assertions.assertEquals(phone, user.getPhone());
    }

    @Test
    void delete() {
        User user = createRandomUser();
        Assertions.assertDoesNotThrow(() -> userService.delete(user.getId()));
        Assertions.assertThrows(NotFoundObjectException.class, () -> userRepository.getOrThrow(user.getId()));
    }

    public static User createRandomUser() {
        String login = "user" + RANDOM.nextInt(1000);
        return createUser(login, getRandomRole(), login + "@mds.ru");
    }

    public static User createUser(String email) {
        return createUser("user" + RANDOM.nextInt(1000), getRandomRole(), email);
    }

    public static User createUser(String login, Role role) {
        return createUser(login, role, randomString() + "@mds.ru");
    }

    public static User createUser(String login, Role role, String email) {
        return userService.create(null, null, null,
                email, null, login, "123456", role, null);
    }

    public static User getAdmin() {
        String login = "admin";
        return userRepository.findByLoginIgnoreCase(login)
                .orElseGet(() -> createUser(login, Role.ADMINISTRATOR));
    }

    public static Role getRandomRole() {
        return roles.get(RANDOM.nextInt(roles.size()));
    }
}
