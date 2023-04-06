package hu.ponte.hr.repository;

import hu.ponte.hr.controller.ImageMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ImageRepositoryTest {
    public static final String ID = "1";
    private ImageMeta testImage;
    @Autowired
    private ImageRepository repository;
    @BeforeEach
    public void setup(){
        testImage = ImageMeta.builder()
                .id(ID)
                .digitalSign("signed")
                .name("test_name")
                .build();
    }
    @Test
    public void test_save_whenImageSavedToDatabase_thenDigitalSignIsEqual(){
        //given
        //when
        ImageMeta imageDao = repository.save(testImage);
        //then
        then(imageDao.getDigitalSign()).isEqualTo(testImage.getDigitalSign());
    }
    @Test
    public void test_findById_givenSavedImage_whenSaved_thenTheStoredImageDigitalSignEquals () {
        //given
        ImageMeta storeImage = repository.save(testImage);
        //when
        ImageMeta imageDao = repository.findById(ID).get();
        //then
        then(imageDao.getDigitalSign()).isEqualTo(storeImage.getDigitalSign());
    }
}