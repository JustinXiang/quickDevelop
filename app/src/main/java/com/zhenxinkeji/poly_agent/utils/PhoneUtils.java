package com.zhenxinkeji.poly_agent.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

    /**
     * 中国手机号码
     */
    private static Pattern CHINESE_PHONE_PATTERN = Pattern.compile("((13|15|17|18)\\d{9})|(14[57]\\d{8})");


    /**
     * 是否是有效的中国手机号码
     * @param phone
     * @return
     */
    public static boolean isValidChinesePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }

        Matcher matcher = CHINESE_PHONE_PATTERN.matcher(phone);
        return matcher.matches();
    }


    /**
     * 检查手机是否无效
     * @param phone
     * @return
     */
    public static boolean isNotValidChinesePhone(String phone) {
        return !isValidChinesePhone(phone);
    }


    /**
     * 手机中间添加星号
     * @param phone
     * @param beginIndex
     * @param endIndex
     * @return empty string if phone length is illegal
     */
    public static String setAsterisk(String phone, int beginIndex, int endIndex) {

//        if (StringUtils.isBlank(phone)) {
//            return StringUtils.EMPTY;
//        }

        if (beginIndex < 0 || endIndex < 0 || beginIndex > phone.length() || endIndex > phone.length()) {
            throw new IllegalArgumentException("illegal index " + beginIndex + "," + endIndex);
        }

        StringBuilder phoneWithAsterisk = new StringBuilder(phone.substring(0, beginIndex));

        for (int i = beginIndex; i < endIndex; i++) {
            phoneWithAsterisk.append("*");
        }

        phoneWithAsterisk.append(phone.substring(endIndex, phone.length()));
        return phoneWithAsterisk.toString();
    }

    /**
     * 手机中间添加星号
     * @param phone
     * @return
     */
    public static String setAsterisk(String phone) {
        return setAsterisk(phone, 3, 7);
    }

    /**
     * 手机中间添加星号,中间六位
     * @param phone
     * @return
     */
    public static String setAsterisk2(String phone) {
        return setAsterisk(phone, 3, 9);
    }
}
