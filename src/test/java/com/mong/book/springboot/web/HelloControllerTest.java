package com.mong.book.springboot.web;

import com.mong.book.springboot.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = HelloController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)

})
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles = "USER")
    @Test
    public void hello가_리턴되다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(get("/hello/dto").param("name", name).param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }

}

// @ExtendWith(SpringExtension.class)
// - 테스트를 진행할때 Junit 에 내장된 실행자 외의 다른 실행자를 실행시킴.
// - SpringExtension 라는 실행자를 실행한것임.
// - 스프링 부트 테스트와 Junit 사이의 연결자 역할을 한다.

// @WebMvcTest(controllers = HelloController.class)
// - 여러 스프링 테스트 어노테이션중 Web(Spring mvc)에 집중할수 있는 어노테이션,
// - 선언할 경우 @Controller , @ControllerAdvice 등을 사용할수 있다.
// - 단 @Service,  @Component , @Repository 등은 사용할수 없다.

// private MockMvc mvc;
// - 웹 API 를 테스트할때 사용.
// - 스프링 MVC 테스트의 시작점.
// - 이 클래스를 통해 HTTP GET, POST 등에 대한 API  테스트를 할수 있다.

// mvc.perform(get("/hello")
// - MockMvc 를 통해 /hello 주소로 GET 요청을 보냄.
// - 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언할수 있습니다.

// .andExpect(status().isOk())
// - mvc.perform 의 결과를 검증
// - HTTP Header 의 Status 를 검증.
// - HTTP Header 의 Status 가 200(ok) 가 맞냐 ?

// .andExpect(content().string(hello));
// - 응답 본문의 내용을 검증.
// - 응답 본문의 내용이 hello 가 맞냐 ?

// param()
// - API 테스트시 사용될 요청 파라미터를 설정.
// - String 만 허용
// - 숫자,날짜 등을 검증할때는 문자열로 변경해야만 한다.

// jsonPath
// - Json 응답값을 필드별로 검증할수 있는 메서드.
// - $를 기준으로 필드명을 명시.