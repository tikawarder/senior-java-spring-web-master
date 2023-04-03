package hu.ponte.hr.controller.upload;

import hu.ponte.hr.exceptions.FileSizeLimitException;
import hu.ponte.hr.services.ImageStore;
import hu.ponte.hr.services.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequestMapping("api/file")
public class UploadController {
    private static final long MAX_FILE_SIZE_IN_BYTES = 2000000L;
    private static final String TOO_LARGE_FILE_ERROR_MESSAGE = " file size is larger than " + MAX_FILE_SIZE_IN_BYTES +
            " bytes";
    @Autowired
    private SignService signService;
    @Autowired
    private ImageStore storeService;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> handleFormUpload(@RequestParam("file") MultipartFile file) throws Exception {
        if(file.getSize() > MAX_FILE_SIZE_IN_BYTES) {
            throw new FileSizeLimitException(file.getOriginalFilename() + TOO_LARGE_FILE_ERROR_MESSAGE);
        }
        String digitalSign = signService.createDigitalSignature(file);
        storeService.store(file, digitalSign);
        return new ResponseEntity<>(digitalSign, HttpStatus.CREATED);
    }
}
