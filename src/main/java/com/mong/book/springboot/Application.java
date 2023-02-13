package com.mong.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


//Application 클래스는 앞으로 만들 프로젝트의 메인 클래스.
//@SpringBootApplication 으로 인해 스프링 부트의 자동설정, 스프링 빈 읽기,생성 을 모두 자동으로 설정.
//따라서 프로젝트 최상단에 위치해야한다.

//@EnableJpaAuditing
// JpaAuditing 활성화.

// //@EnableJpaAuditing 를 사용하려면 하나 이상의 Entity 가 필요하다.
// @WebMvcTest 를 사용하려면 Entity 가 없어서 사용 불가하므로 따로 빼려고 지움.