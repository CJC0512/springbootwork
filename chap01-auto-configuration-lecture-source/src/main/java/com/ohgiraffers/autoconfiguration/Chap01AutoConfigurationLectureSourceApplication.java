package com.ohgiraffers.autoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* 필기. 매우매우 중요한 어노테이션. 해당 어노테이션이 거의 다 해준다고 보면 된다. (dependencies 같은 것들 +a)*/
@SpringBootApplication
public class Chap01AutoConfigurationLectureSourceApplication {

    public static void main(String[] args) {

        /* 필기. 실행과 동시에 Tomcat 서버가 시작됨 */
        SpringApplication.run(Chap01AutoConfigurationLectureSourceApplication.class, args);
    }

}
