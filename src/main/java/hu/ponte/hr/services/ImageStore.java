package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ImageStore {
    public static final String SUCCESS_SAVE_MESSAGE = " image stored in the database";
    public static final String SUCCESS_FIND_IN_DATABASE_MESSAGE = " preview got fom database";
    @Autowired
    private ImageRepository repository;

    public void store(MultipartFile file, String digitalSign) throws IOException {
        ImageMeta storedImage = repository.save(ImageMeta.builder()
                .name(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .size(file.getSize())
                .digitalSign(digitalSign)
                .imageData(file.getBytes())
                .build());
        log.info(storedImage.getName() + SUCCESS_SAVE_MESSAGE);
    }

    public ImageMeta getImageMetaById(String id){
        ImageMeta imageById = repository.findById(id).orElseThrow(NoSuchElementException::new);
        log.info(imageById.getName() + SUCCESS_FIND_IN_DATABASE_MESSAGE);
        return imageById;
    }

    public List<ImageMeta> getAllImageMeta(){
        return repository.findAll();
    }
}
