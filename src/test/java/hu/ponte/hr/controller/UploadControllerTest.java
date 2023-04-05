package hu.ponte.hr.controller;

import hu.ponte.hr.services.ImageStore;
import hu.ponte.hr.services.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;
import static hu.ponte.hr.SignTest.CONTENT_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UploadControllerTest {
    public static final String SIGNATURE = "signature";
    private MultipartFile multipartFile;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SignService signService;
    @MockBean
    private ImageStore storeService;
    @BeforeEach
    public void setUp() {
        byte[] testImageBytes = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        multipartFile = new MockMultipartFile("test_file",
                "test.jpg", CONTENT_TYPE, testImageBytes);
    }
    @Test
    public void test_handleFormUpload_givenMockedServices_thenStatusCreatedAndSignature() throws Exception {
        //given
        given(signService.createDigitalSignature(any(MultipartFile.class))).willReturn(SIGNATURE);
        doNothing().when(storeService).store(multipartFile, SIGNATURE);
        //when then
        mockMvc.perform(multipart("/api/file/post")
                .file("file", multipartFile.getBytes()))
                .andExpect(status().isCreated())
                .andExpect(content().string(SIGNATURE));
    }
}
