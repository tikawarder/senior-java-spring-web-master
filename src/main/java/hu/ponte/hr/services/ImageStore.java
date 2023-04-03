package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ImageStore {
    @Autowired
    private ImageRepository repository;

    public void store(MultipartFile file, String digitalSign) throws IOException {
        repository.save(ImageMeta.builder()
                .name(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .size(file.getSize())
                .digitalSign(digitalSign)
                .imageData(file.getBytes())
                .build());
    }

    public ImageMeta getImageMetaById(String id){
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<ImageMeta> getAllImageMeta(){
        return repository.findAll();
    }
}
