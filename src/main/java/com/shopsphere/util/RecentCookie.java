package com.shopsphere.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

// Stores up to 5 recently viewed product IDs in a cookie named "recent".
// Only IDs are stored: no names, passwords, or personal data.
public class RecentCookie {

    public static final String NAME = "recent";
    private static final int MAX = 5;

    public static List<Integer> read(HttpServletRequest req) {
        List<Integer> ids = new ArrayList<>();
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return ids;
        }
        for (Cookie c : cookies) {
            if (NAME.equals(c.getName())) {
                for (String part : c.getValue().split("-")) {
                    try {
                        ids.add(Integer.parseInt(part));
                    } catch (NumberFormatException ignored) {
                        // skip anything that isn't an ID
                    }
                }
            }
        }
        return ids;
    }

    // Moves the ID to the front, removing any earlier copy
    public static void remember(List<Integer> ids, int id) {
        ids.remove(Integer.valueOf(id));
        ids.add(0, id);
        while (ids.size() > MAX) {
            ids.remove(ids.size() - 1);
        }
    }

    public static void write(HttpServletResponse resp, String contextPath, List<Integer> ids) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                value.append('-');
            }
            value.append(ids.get(i));
        }
        Cookie cookie = new Cookie(NAME, value.toString());
        cookie.setPath(contextPath.isEmpty() ? "/" : contextPath);
        cookie.setMaxAge(30 * 24 * 60 * 60);   // 30 days
        cookie.setHttpOnly(true);              // JavaScript cannot read it
        resp.addCookie(cookie);
    }
}