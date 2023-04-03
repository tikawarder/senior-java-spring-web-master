package hu.ponte.hr.controller;


import hu.ponte.hr.services.ImageStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@Slf4j
@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
        return imageStore.getAllImageMeta();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        ImageMeta image = imageStore.getImageMetaById(id);
        InputStream inputStream = new ByteArrayInputStream(image.getImageData());
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @ExceptionHandler(IOException.class)
    private ResponseEntity<String> handleIOErrors(IOException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
