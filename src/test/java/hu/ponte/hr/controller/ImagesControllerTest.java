package hu.ponte.hr.controller;

import hu.ponte.hr.services.ImageStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ImagesControllerTest {
    public static final String ID = "1";
    public static final String TEST_IMAGE_NAME = "test.image";
    private byte [] testImageBytes;
    private ImageMeta testImage;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageStore imageStore;
    @BeforeEach
    public void setUp(){
        testImageBytes = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        testImage = ImageMeta.builder()
                .id(ID)
                .name(TEST_IMAGE_NAME)
                .imageData(testImageBytes)
                .build();
    }
    @Test
    public void test_getImage_givenTestImage_thenStatusOKAndImageArrayReturns() throws Exception {
        //given
        given(imageStore.getImageMetaById(ID)).willReturn(testImage);
        //when then
        mockMvc.perform(get("/api/images/preview/1"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(testImageBytes));
    }
    @Test
    public void test_listImages_thenStatusOkAndListOfAllImagesReturns() throws Exception {
        //given
        List<ImageMeta> imageMetas = List.of(testImage);
        given(imageStore.getAllImageMeta()).willReturn(imageMetas);
        //when then
        mockMvc.perform(get("/api/images/meta"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", equalTo(TEST_IMAGE_NAME)));
    }
}