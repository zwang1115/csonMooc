package com.cspiration.mooc.interception;

import com.cspiration.mooc.entity.UserAuth;
import com.cspiration.mooc.utils.CommonConstants;
import com.cspiration.mooc.utils.ResultMsg;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String PATTERN_STATIC = "/static";
    private static final String PATTERN_ERROR = "/error";
    private static final String PATTERN_TARGET = "target";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> patameterMap = request.getParameterMap();
        patameterMap.forEach((k, v) -> {
            if (k.equals(ResultMsg.errorMsgKey) || k.equals(ResultMsg.successMsgKey) || k.equals(PATTERN_TARGET)) {
                request.setAttribute(k, Joiner.on(",").join(v));
            }
        });

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(PATTERN_STATIC) || requestURI.startsWith(PATTERN_ERROR)) {
            return true;
        }
        HttpSession session = request.getSession(true);
        UserAuth userAuth = (UserAuth) session.getAttribute(CommonConstants.USER_ATTRIBUTE);
        if (userAuth != null) {
            UserContext.setUser(userAuth);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
