package com.cft.focusstart.library.controller.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class ControllerUtil {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static void testUtilPost(MockMvc mockMvc, String url, Object inData, String outData, ResultMatcher resultMatcher) throws Exception{
        mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(convertObjectToJsonBytes(inData))
        )
                .andExpect(resultMatcher)
                .andExpect(content().string(containsString(outData)));

    }

    public static void testUtilGet(MockMvc mockMvc, String url, String outData, ResultMatcher resultMatcher) throws Exception{
        mockMvc.perform(
                get(url)
        )
                .andExpect(resultMatcher)
                .andExpect(content().string(containsString(outData)));

    }
}
