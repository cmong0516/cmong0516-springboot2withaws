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

//    @Test
    @WithMockUser(roles = "USER")
    public void Posts_??????() throws Exception {
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

//    @Test
    @WithMockUser(roles = "USER")
    public void Posts_??????() throws Exception {
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

// HelloController ?????? ???????????? @WebMvcTest ??? ???????????? ?????????.
// - @WebMvcTest ??? ?????? JPA ????????? ???????????? ?????? ??????.
// JPA ???????????? ?????? @SpringBootTest ??? TestRestTemplate ??? ????????????.

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) ?????????????
// - ???????????? ??? ????????? ???????????? ???????????? ???????????? SpringBootTest.WebEnvironment.Mock ??????.
// Mock ??? ?????? ????????? ??????????????? ????????? ?????? ????????? ??????????????? Mocking ????????? ????????????
// ????????? WebEnvironment.RANDOM_PORT ??? ???????????? ?????? ??????????????? ???????????? @LocalServerPort ??? ??????
// ????????? ?????? ???????????? ????????????.


// @LocalServerPort ??? ?????? ?
// -webEnvironment.RANDOM_PORT ????????? @LocalServerPort ??? ?????? ????????? ?????? ????????????.

// ??????
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) ??????
// @LocalServerPost ??? ?????? ???????????? port ????????? ???????????? ????????????.


// private TestRestTemplate restTemplate; ??? ?????? ?
// - HTTP Client Rest Api ????????? ?????? ????????? ???????????? ?????????.

// postForEntity ??? ?????? ?
// - 	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
//			Object... urlVariables) throws RestClientException {
//		return this.restTemplate.postForEntity(url, request, responseType, urlVariables);
//	}
// - url , requestObject , ResponseType ??? ??????????????? ?????? ResponseEntity ??? ??????????????? ?