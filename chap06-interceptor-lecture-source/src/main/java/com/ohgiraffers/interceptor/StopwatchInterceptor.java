package com.ohgiraffers.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/* 설명.
 *   인터셉터를 사용하는 경우(목적)
 *   : 로그인 체크, 권한 체크, 프로그램 실행 시간 계산 작업 로그 처리, 업로드 파일 처리, 로케일(지역) 설정 등
 * */
@Configuration
public class StopwatchInterceptor implements HandlerInterceptor {

    /* 설명. 필터와 달리 인터셉터는 빈을 활용할 수 있다. (생성자 주입 활용) */
    private final MenuService MENUSERVICE;

    @Autowired
    public StopwatchInterceptor(MenuService MENUSERVICE) {
        this.MENUSERVICE = MENUSERVICE;
    }

    /* 설명. boolean형에 따라 핸들러 메소드가 실행 될 수도 있고 안 될 수도 있도록 할 수 있으며,
    *       핸들러 메소드 이전 전처리 목적이다.
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /* 설명. Handler Interceptor는 Bean을 활용할 수 있다. (@Service 계층의 객체도 bean이다.) */
//        MENUSERVICE.method(); // bean을 활용할 수 있음을 확인할 수 있다.
        System.out.println("preHandle 호출함...(핸들러 메소드 이전)");

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;

        /* 설명. 반환형을 false로 하면 특정 조건에 의해 이후 핸들러 메소드가 실행되지 않게 할 수도 있다. */
//        return false;       // 핸들러 메소드가 동작하지 않는다.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("preHandle 호출함...(핸들러 메소드 이후)");

        long startTime = (long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();

        request.removeAttribute("startTime");   // 필요 없는 속성 제거

        modelAndView.addObject("interval", endTime - startTime);



    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }


}
