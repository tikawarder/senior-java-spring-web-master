package hu.ponte.hr.controller.upload;

import hu.ponte.hr.services.ImageStore;
import hu.ponte.hr.services.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Component
@RequestMapping("api/file")
public class UploadController {
    @Autowired
    private SignService signService;
    @Autowired
    private ImageStore storeService;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> handleFormUpload(@RequestParam("file") MultipartFile file) throws Exception {
        String digitalSign = signService.createDigitalSignature(file);
        storeService.store(file, digitalSign);
        return new ResponseEntity<>(digitalSign, HttpStatus.CREATED);
    }
}
