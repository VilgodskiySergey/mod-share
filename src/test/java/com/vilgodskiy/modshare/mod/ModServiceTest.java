package com.vilgodskiy.modshare.mod;

import com.elementsoft.common.exception.ValidationException;
import com.vilgodskiy.modshare.application.exception.NotFoundObjectException;
import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static com.vilgodskiy.modshare.user.UserServiceTest.createUser;
import static com.vilgodskiy.modshare.user.UserServiceTest.getAdmin;
import static com.vilgodskiy.modshare.util.ServiceUtil.*;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ModServiceTest {

    @Test
    void createRandom() {
        Assertions.assertDoesNotThrow(ModServiceTest::createRandomMod);
    }

    @Test
    void create() {
        String title = randomString(Mod.TITLE_LENGTH);
        User owner = createUser(randomString(), Role.MOD_DEVELOPER);
        Mod mod = Assertions.assertDoesNotThrow(() -> modService.create(title, owner));
        Assertions.assertEquals(title, mod.getTitle());
        Assertions.assertEquals(owner.getId(), mod.getOwner().getId());
        Assertions.assertNotNull(mod.getId());
        Assertions.assertNotNull(mod.getTracingInfo());
        Assertions.assertNull(mod.getRating());
    }

    @Test
    void createWithIncorrectTitle() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> createMod(null));
        Assertions.assertEquals(1, exception.getErrors().size());
        String emptyTitle = "";
        exception = Assertions.assertThrows(ValidationException.class, () -> createMod(emptyTitle));
        Assertions.assertEquals(1, exception.getErrors().size());
        String tooLongTitle = randomString(Mod.TITLE_LENGTH + 1);
        exception = Assertions.assertThrows(ValidationException.class, () -> createUser(tooLongTitle));
        Assertions.assertEquals(1, exception.getErrors().size());
    }

    @Test
    void createWithWrongOwner() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> createMod(randomString(Mod.TITLE_LENGTH), null));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(Mod.Validators.EMPTY_OWNER, exception.getErrors().get(0));
        User consumer = createUser(randomString(User.LOGIN_MAX_LENGTH), Role.CONSUMER);
        exception = Assertions.assertThrows(ValidationException.class,
                () -> createMod(randomString(Mod.TITLE_LENGTH), consumer));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(Mod.Validators.OWNER_INCORRECT_ROLE, exception.getErrors().get(0));
        exception = Assertions.assertThrows(ValidationException.class,
                () -> createMod(randomString(Mod.TITLE_LENGTH), getAdmin()));
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(Mod.Validators.OWNER_INCORRECT_ROLE, exception.getErrors().get(0));
    }

    @Test
    void update() {
        Mod mod = createRandomMod();
        String title = randomString(Mod.TITLE_LENGTH);
        Mod updatedMod = Assertions.assertDoesNotThrow(() -> modService.update(mod.getId(), title,
                mod.getOwner()));
        Assertions.assertEquals(title, updatedMod.getTitle());
    }

    @Test
    void delete() {
        Mod mod = createRandomMod();
        Assertions.assertDoesNotThrow(() -> modService.delete(mod.getId()));
        Assertions.assertThrows(NotFoundObjectException.class, () -> modRepository.getOrThrow(mod.getId()));
    }

    public static Mod createRandomMod() {
        return createMod(randomString());
    }

    public static Mod createMod(String title) {
        return createMod(title, createUser(randomString(), Role.MOD_DEVELOPER));
    }

    public static Mod createMod(String title, User owner) {
        return modService.create(title, owner);
    }

}
