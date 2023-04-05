package hu.ponte.hr.controller;

import hu.ponte.hr.services.ImageStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ImagesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageStore imageStore;

    @Test
    void test_getImage_givenTestImage_thenStatusOKAndImageArrayReturns() throws Exception {
        byte [] testImageBytes = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        //given
        given(imageStore.getImageMetaById(anyString())).willReturn(
                ImageMeta.builder()
                        .id("1")
                        .name("test.image")
                        .imageData(testImageBytes)
                        .build()
        );
        //when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/images/preview/1"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(testImageBytes));

    }
}