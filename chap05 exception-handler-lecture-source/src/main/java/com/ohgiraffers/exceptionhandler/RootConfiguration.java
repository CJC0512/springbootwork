package com.ohgiraffers.exceptionhandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class RootConfiguration {

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties props = new Properties();
        props.setProperty("java.lang.NullPointerException", "error/nullPointer");
        props.setProperty("MemberRegistException", "error/memberRegist");


        /* 설명. 전체 예외 관련되어 SimpleMappingExceptionResolver에 설정하기 */

        /* 설명. 1. 예외에 따른 페이지 설정한 것 활용 */
        exceptionResolver.setExceptionMappings(props);

        /* 설명. 2. 그 외 나머지 예외에 대한 deafult 페이지 설정 */
        exceptionResolver.setDefaultErrorView("error/default");

        /* 설명. 3. 예외 메시지를 뽑기 위한 Attribute Key 값 (화면에서 예외 메시지 확인 시 사용할 값)*/
        exceptionResolver.setExceptionAttribute("exceptionMessage");
        return exceptionResolver;
    }
}
