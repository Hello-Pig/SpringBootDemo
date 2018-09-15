package net.hellopig.app.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class LogInTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLogIn() throws Exception {
        // 构造请求
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login").accept(MediaType.APPLICATION_JSON);
        // 测试
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("登录！")));
    }
}