package com.mong.book.springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension
;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    // 이건 테스트용 HTTP 메서드를 실행할수 있게 해준다고 배움.

    @Test
    public void 메인페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        System.out.println("body = " + body);
        // body 에 index.mustache 가 들어있다.

        //then
        Assertions.assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
    }
}

// SpringExtension 가 뭐임 ??
// - SpringBootTest 만 쓰면 무거운 프로젝트로서의 역할을 할수도 있다.
// - SpringExtension.class 로 지정하면 @Autowired , @MockBean 등 필
// 요로 하는 요소만 컨텍스트가 (스프링이)관리해서 가볍게 테스트를 돌릴수 있다.