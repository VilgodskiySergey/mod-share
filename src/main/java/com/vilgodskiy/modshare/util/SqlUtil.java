package com.vilgodskiy.modshare.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class SqlUtil {

    public static String replacePercentSignForDbSearch(String searchString) {
        return StringUtils.isBlank(searchString)
                ? searchString
                : searchString.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
    }
}
