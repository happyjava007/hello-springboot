package cn.happyjava.core.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class TestUtils {

    public static String getUUID() {
        return RandomStringUtils.random(100);
    }

}
