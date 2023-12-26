package com.binghe.domain.validate;

import java.util.regex.Pattern;

public class IpAddressValidation {

    private static final String IPV4_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final Pattern IPV4_PATTERN_COMPILED = Pattern.compile(IPV4_PATTERN);

    private IpAddressValidation () {
    }

    public static boolean validate(String ipAddress) {
        return IPV4_PATTERN_COMPILED.matcher(ipAddress).matches();
    }
}
