package com.hospital.util;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {

	

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        cookie.setDomain("");
        
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    	
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())));
    }
    
    


//    public HttpCookie createAccessTokenCookie(String token, Long duration) {
//        String encryptedToken = SecurityCipher.encrypt(token);
//        return ResponseCookie.from(accessTokenCookieName, encryptedToken)
//                .maxAge(duration)
//                .httpOnly(true)
//                .path("/")
//                .secure(false)
//                
//                .build();
//    }
//
//    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
//        String encryptedToken = SecurityCipher.encrypt(token);
//        return ResponseCookie.from(refreshTokenCookieName, encryptedToken)
//                .maxAge(duration)
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
////                .domain("192.168.1.50")
//                .build();
//    }

//    public HttpCookie deleteAccessTokenCookie() {
//        return ResponseCookie.from(accessTokenCookieName, "").maxAge(0).httpOnly(true).path("/").build();
//    }
//    public HttpCookie deleteRefreshTokenCookie() {
//    	return ResponseCookie.from(refreshTokenCookieName, null).maxAge(0).httpOnly(true).path("/").build();
//    }
    

}
