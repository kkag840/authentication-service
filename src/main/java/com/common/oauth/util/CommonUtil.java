package com.common.oauth.util;

import java.util.regex.Pattern;

public class CommonUtil {

    public static boolean isMobile(String userAgent){
        Pattern pattern = Pattern.compile(Constants.mobileRegex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(userAgent).matches();
    }
}
