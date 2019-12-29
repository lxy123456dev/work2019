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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    @Autowired
    private StringRedisTemplate redisTemplate;
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
            UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), "role_user");
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
            String token = CookieUtils.getCookieValue(request, prop.getUser().getCookieName(), "utf-8");
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserInfo.class);
            String id = payload.getId();
            Boolean boo = redisTemplate.hasKey(id);
            if (boo != null && boo) {
                throw new LyException(ResponseCode.UNAUTHORIZED);
            }
            Date expiration = payload.getExpiration();
            DateTime refreshTime = new DateTime(expiration.getTime()).minusMinutes(prop.getUser().getMinRefreshTime());
            if (refreshTime.isBefore(System.currentTimeMillis())) {
                // 如果过了刷新时间，则生成新token
                token = JwtUtils.generateTokenExpireInMinutes(payload.getUserInfo(), prop.getPrivateKey(), prop.getUser().getExpire());
                // 写入cookie
                CookieUtils.newCookieBuilder()
                        .response(response)
                        .httpOnly(true)
                        .domain(prop.getUser().getCookieDomain())
                        .name(prop.getUser().getCookieName())
                        .value(token)
                        .build();
            }
            return payload.getUserInfo();
        } catch (Exception e) {
            throw new LyException(ResponseCode.UNAUTHORIZED);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, prop.getUser().getCookieName());
        Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), UserInfo.class);
        long time = payload.getExpiration().getTime() - System.currentTimeMillis();
        String id = payload.getId();
        if (time > 5000) {
            redisTemplate.opsForValue().set(id, "", time, TimeUnit.MILLISECONDS);
        }
        CookieUtils.deleteCookie(prop.getUser().getCookieName(), prop.getUser().getCookieDomain(), response);
    }
}
