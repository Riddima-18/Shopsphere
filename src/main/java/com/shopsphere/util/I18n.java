package com.shopsphere.util;

import jakarta.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    public static final String SESSION_KEY = "locale";

    // Reads the language from the session. Defaults to English.
    public static Locale localeOf(HttpServletRequest req) {
        Object saved = req.getSession().getAttribute(SESSION_KEY);
        return saved instanceof Locale ? (Locale) saved : Locale.ENGLISH;
    }

    // Accepts only "en" or "hi", so a bad value can't change the language
    public static Locale parse(String code) {
       return "hi".equals(code) ? Locale.of("hi", "IN") : Locale.ENGLISH;
    }

    public static ResourceBundle bundle(Locale locale) {
        return ResourceBundle.getBundle("messages", locale);
    }

    // Formats a number for the locale, with two decimal places
    public static String money(double amount, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(amount);
    }
}