package cn.happyjava.springbootspringsecurityjwtmybatisplus.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Happy
 */
public class CookiesUtils {

    /**
     * 设置cookie
     *
     * @param expired 有效时间(s)
     */
    public static void setCookies(HttpServletResponse response, String name, String value, int expired) throws UnsupportedEncodingException {
        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookies = new Cookie(name, value);
        cookies.setMaxAge(expired);
        cookies.setPath("/");
        response.addCookie(cookies);
    }

    /**
     * 设置cookie
     */
    public static void setCookies(HttpServletResponse response, String name, String value) {
        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookies = new Cookie(name, value);
        cookies.setPath("/");
        response.addCookie(cookies);
    }

    /**
     * 获得cookie
     */
    public static String getValue(HttpServletRequest request, String name) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), name)) {
                return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

}
