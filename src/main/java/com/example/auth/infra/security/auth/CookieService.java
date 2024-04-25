package com.example.auth.infra.security.auth;

import com.example.auth.domain.utils.Utils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${api.security.cookie.name}")
    private String cookieName;

    public void createCookieWithToken(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(false);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public String recoverTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    if (Utils.isStringPresent(cookie.getValue())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    public void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("rede_estrela_auth_token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
