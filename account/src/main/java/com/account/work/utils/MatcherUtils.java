package com.account.work.utils;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular expressions and other formatting related tools
 */

public class MatcherUtils {
    /**
     * Check if the mailbox format is correct
     */
    public static boolean isEmail(String email) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        // Regular expression patterns
        Pattern p = Pattern.compile(RULE_EMAIL);
        // Matcher for regular expressions
        Matcher m = p.matcher(email);
        // Regular matching
        return m.matches();
    }

    /**
     * Get the amount formatting tool
     * When a decimal data is large, it does not automatically display as a scientific counting method
     */
    public static NumberFormat getMoneyFormat() {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(1);
        return nf;
    }
}
