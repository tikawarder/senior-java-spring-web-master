package hu.ponte.hr;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.repository.ImageRepository;
import hu.ponte.hr.services.ImageStore;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.List;
import java.util.Optional;
class ImageStoreTest {
    @Mock
    private ImageRepository repository;
    @InjectMocks
    private ImageStore underTest;
    private MultipartFile file;
    private String signature;
    private ImageMeta image;
    private ImageMeta savedImage;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        file = new MockMultipartFile("test_file", "cat.jpg", "image/jpeg",
                new FileInputStream("src/test/resources/images/cat.jpg"));
        signature = "test_signature";
        image = ImageMeta.builder()
                .name(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .size(file.getSize())
                .digitalSign(signature)
                .imageData(file.getBytes())
                .build();
        savedImage = ImageMeta.builder()
                .name("saved.image")
                .build();
    }
    @Test
    void test_store_givenSetUp_whenRepositoryMocked_thenRepositorySaveVerified() throws IOException {
        //given
        //when
        Mockito.when(repository.save(image)).thenReturn(savedImage);
        underTest.store(file, signature);
        //then
        Mockito.verify(repository).save(image);
    }

    @Test
    void test_getImageMetaById_givenTestId_whenRepositoryMocked_thenSavedImageReturns() {
        //given
        String testId = "test_id";
        //when
        Mockito.when(repository.findById(testId)).thenReturn(Optional.of(savedImage));
        ImageMeta result = underTest.getImageMetaById(testId);
        //then
        Assert.assertEquals(savedImage, result);
        Mockito.verify(repository).findById(testId);
    }

    @Test
    void test_getAllImageMeta_givenList_whenRepositoryMocked_thenListReturns() {
        //given
        List<ImageMeta> imageMetas = List.of(savedImage);
        //when
        Mockito.when(repository.findAll()).thenReturn(imageMetas);
        List<ImageMeta> results = underTest.getAllImageMeta();
        //then
        Assert.assertEquals(imageMetas, results);
        Mockito.verify(repository).findAll();
    }
}