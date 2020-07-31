package com.vilgodskiy.modshare.util;

import com.vilgodskiy.modshare.user.repository.UserRepository;
import com.vilgodskiy.modshare.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Component
public class ServiceUtil {

    public final static Random RANDOM = new Random();

    public static UserService userService;
    public static UserRepository userRepository;

    public static String randomString() {
        return randomString(16, true, true);
    }

    public static String randomString(int length) {
        return randomString(length, true, true);
    }

    public static String randomString(int length, boolean letters, boolean numbers) {
        return RandomStringUtils.random(length, letters, numbers);
    }

    public static String randomString(int length, char startSymbol, char endSymbol) {
        return RandomStringUtils.random(length, startSymbol, endSymbol);
    }

    @Autowired
    public void setUserService(UserService userService) {
        ServiceUtil.userService = userService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        ServiceUtil.userRepository = userRepository;
    }
}
