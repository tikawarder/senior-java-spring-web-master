package hu.ponte.hr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Service
public class SignService {
    private static final String CRYPT_ALGORITHM = "SHA256withRSA";
    private static final String PRIVATE_KEY_PATH = "src/main/resources/config/keys/key.private";
    private static final String PUBLIC_KEY_PATH = "src/main/resources/config/keys/key.pub";
    public static final String KEY_TYPE = "RSA";
    public static final String ENCODING_MESSAGE = "Encoding digital signature";
    public static final String DECODING_MESSAGE = "Decoding digital signature";

    public String createDigitalSignature(MultipartFile file) throws Exception {
        Signature signature = Signature.getInstance(CRYPT_ALGORITHM);
        signature.initSign(getPrivateKey());
        byte[] messageBytes = file.getBytes();
        signature.update(messageBytes);
        return encodeSignature(signature.sign());
    }

    public boolean isSignedDocumentValid(MultipartFile file, String digitalSignature) throws Exception {
        Signature signature = Signature.getInstance(CRYPT_ALGORITHM);
        signature.initVerify(getPublicKey());
        byte[] messageBytes = file.getBytes();
        signature.update(messageBytes);
        return signature.verify(decodeSignature(digitalSignature));
    }

    private PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = Files.readAllBytes(Paths.get(PRIVATE_KEY_PATH));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] key = Files.readAllBytes(Paths.get(PUBLIC_KEY_PATH));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        return keyFactory.generatePublic(keySpec);
    }

    private String encodeSignature(byte[] digitalSignature) {
        log.info(ENCODING_MESSAGE);
        return Base64.getMimeEncoder().encodeToString(digitalSignature);
    }

    private byte[] decodeSignature(String encodedSignature) {
        log.info(DECODING_MESSAGE);
        return Base64.getMimeDecoder().decode(encodedSignature);
    }
}
