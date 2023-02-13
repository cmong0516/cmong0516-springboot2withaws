package com.mong.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mong.book.springboot.web.domain.posts.Posts;
import com.mong.book.springboot.web.domain.posts.PostsRepository;
import com.mong.book.springboot.web.dto.PostsSaveRequestDto;
import com.mong.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension
;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void testDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_등록() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

//        System.out.println("responseEntity = " + responseEntity);
//        responseEntity = <200,1,[Content-Type:"application/json;charset=UTF-8", Transfer-Encoding:"chunked", Date:"Thu, 20 Oct 2022 14:29:07 GMT"]>

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_수정() throws Exception {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(expectedTitle).content(expectedContent).build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

//        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

}

// HelloController 에서 사용했던 @WebMvcTest 를 사용하지 않았다.
// - @WebMvcTest 의 경우 JPA 기능이 작동하지 않기 때문.
// JPA 테스트를 할땐 @SpringBootTest 와 TestRestTemplate 를 사용하자.

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 이거뭐임?
// - 테스트의 웹 한경을 설정하는 속성이며 기본값은 SpringBootTest.WebEnvironment.Mock 이다.
// Mock 은 실제 서블릿 컨테이너를 띄우지 않고 서블릿 컨테이너를 Mocking 한것이 실행된다
// 여기선 WebEnvironment.RANDOM_PORT 를 사용하면 실제 컨테이너를 호출하며 @LocalServerPort 가 붙은
// 변수에 값을 주입하여 실행한다.


// @LocalServerPort 가 뭐임 ?
// -webEnvironment.RANDOM_PORT 설정시 @LocalServerPort 가 붙은 변수에 값을 주입한다.

// 고로
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 쓰면
// @LocalServerPost 가 값을 주입받아 port 변수가 랜덤하게 생성된다.


// private TestRestTemplate restTemplate; 가 뭐임 ?
// - HTTP Client Rest Api 호출을 위한 함수를 제공하는 클래스.

// postForEntity 가 뭐임 ?
// - 	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
//			Object... urlVariables) throws RestClientException {
//		return this.restTemplate.postForEntity(url, request, responseType, urlVariables);
//	}
// - url , requestObject , ResponseType 을 매개변수로 받아 ResponseEntity 를 반환해주네 ?