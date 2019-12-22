package com.leyou.auth.service;

import com.leyou.auth.Payload;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.exception.LyException;
import com.leyou.exception.enums.ResponseCode;
import com.leyou.user.UserClient;
import com.leyou.user.UserDTO;
import com.leyou.utils.CookieUtils;
import com.leyou.utils.JwtUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class AuthService {
    @Autowired
    private JwtProperties prop;

    @Autowired
    private UserClient userClient;

    //private static final String USER_ROLE = "role_user";

    public void login(String username, String password, HttpServletResponse response) {
        try {
            // 查询用户
            UserDTO user = userClient.queryUserByUsernameAndPassword(username, password);
            // 生成userInfo, 没写权限功能，暂时都用guest
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), "guest");
            // 生成token
            String token = JwtUtils.generateTokenExpireInMinutes(userInfo, prop.getPrivateKey(), prop.getUser().getExpire());
            // 写入cookie
            CookieUtils.newCookieBuilder()
                    .response(response) // response,用于写cookie
                    .httpOnly(true) // 保证安全防止XSS攻击，不允许JS操作cookie
                    .domain(prop.getUser().getCookieDomain()) // 设置domain
                    .name(prop.getUser().getCookieName()).value(token) // 设置cookie名称和值
                    .build();// 写cookie
        } catch (Exception e) {
            throw new LyException(ResponseCode.INVALID_USERNAME_PASSWORD);
        }
    }

    public UserInfo verifyUser(HttpServletRequest request, HttpServletResponse response) {

        try {
            String cookieValue = CookieUtils.getCookieValue(request, prop.getUser().getCookieName());
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(cookieValue, prop.getPublicKey(), UserInfo.class);
            Date expiration = payload.getExpiration();
            DateTime refreshTime = new DateTime(expiration.getTime()).minusMinutes(prop.getUser().getMinRefreshTime());
            if (refreshTime.isBefore(System.currentTimeMillis())) {
                // 如果过了刷新时间，则生成新token
                cookieValue = JwtUtils.generateTokenExpireInMinutes(payload.getUserInfo(), prop.getPrivateKey(), prop.getUser().getExpire());
                // 写入cookie
                CookieUtils.newCookieBuilder()
                        // response,用于写cookie
                        .response(response)
                        // 保证安全防止XSS攻击，不允许JS操作cookie
                        .httpOnly(true)
                        // 设置domain
                        .domain(prop.getUser().getCookieDomain())
                        // 设置cookie名称和值
                        .name(prop.getUser().getCookieName()).value(cookieValue)
                        // 写cookie
                        .build();
            }
            return payload.getUserInfo();
        } catch (Exception e) {
            throw new LyException(ResponseCode.UNAUTHORIZED);
        }
    }
}
