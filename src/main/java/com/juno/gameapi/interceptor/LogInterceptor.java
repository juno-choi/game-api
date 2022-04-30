package com.juno.gameapi.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    //controller 실행 전
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = UUID.randomUUID().toString();
        StringBuffer requestURL = request.getRequestURL();
        log.info("request url = {}", requestURL.toString());
        log.info("pre log = {}", uuid);
        HttpSession session = request.getSession();
        session.setAttribute(LOG_ID, uuid); //session에 로그에 대한 랜덤 아이디 값을 저장해둠.
        return true;   //return이 false일 경우 다음 실행을 하지 않음, true여야 다음 interceptor or controller가 실행됨
    }

    //controller 실행 후 (예외 발생 시 실행되지 않음)
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession(false);//session이 없다면 만들지 않음
        String logId = (String)session.getAttribute(LOG_ID);

        log.info("post log = {}", logId);
    }

    //요청이 완전히 종료된 후 (예외가 발생해도 실행 됨, 예외가 발생하면 ex로 예외를 처리해줄 수 있음)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HttpSession session = request.getSession(false);//session이 없다면 만들지 않음
        String logId = (String)session.getAttribute(LOG_ID);

        log.info("after log = {}", logId);
    }
}
