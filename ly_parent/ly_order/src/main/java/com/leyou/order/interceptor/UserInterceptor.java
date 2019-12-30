package com.leyou.order.interceptor;


import com.leyou.auth.Payload;
import com.leyou.auth.entity.UserInfo;
import com.leyou.threadlocals.UserHolder;
import com.leyou.utils.CookieUtils;
import com.leyou.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    private static final String cookie_name = "LY_TOKEN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = "";
        try {
            //从cookie获取Token
            token = CookieUtils.getCookieValue(request, cookie_name);
            //解析token 获取用户载荷信息
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, UserInfo.class);
            //存入ThreadLocal
            UserInfo userInfo = payload.getUserInfo();
            UserHolder.setUser(userInfo.getId());
            log.info("【订单微服务】,当前访问用户ID：{}", userInfo.getId());
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("【订单微服务】,获取用户信息失败：令牌：{}", token);
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
