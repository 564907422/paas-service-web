package com.paas.web.utils;


import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lt
 * @date 2017/12/25
 */
public class CookieUtils {
    public static Cookie getCookie(Cookie cookies[], String name) {

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }


    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        return getCookie(cookies, name);
    }


    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(StringUtils.isEmpty(request.getContextPath()) ? "/" : request.getContextPath());
        cookie.setMaxAge(maxAge);
        if (!StringUtils.isEmpty(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }


    public static void removeCookie(HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        if (!StringUtils.isEmpty(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    public static String getSessionId(HttpServletRequest request) {
        String jsessionid = "";
        Cookie cookie = CookieUtils.getCookie(request, "JSESSIONID");
        if (cookie != null) {
            jsessionid = cookie.getValue();
        }
        return jsessionid;
    }
}
