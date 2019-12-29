package com.leyou.cart.interceptors;

import com.leyou.auth.Payload;
import com.leyou.auth.entity.UserInfo;
import com.leyou.threadlocals.UserHolder;
import com.leyou.utils.CookieUtils;
import com.leyou.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = CookieUtils.getCookieValue(request, "LY_TOKEN");
            Payload<UserInfo> payload = JwtUtils.getInfoFromToken(token, UserInfo.class);
            UserHolder.setUser(payload.getUserInfo().getId());
            return true;
        } catch (Exception e) {
            log.error("【购物车服务】解析用户信息失败！", e);
            return false;
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
