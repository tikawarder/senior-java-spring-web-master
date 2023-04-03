package hu.ponte.hr.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

@Service
public class SignService {
    private static final String CRYPT_ALGORITHM = "SHA256withRSA";
    private static final String KEY_FORMAT = "PKCS8";
    private static final String PRIVATE_KEY_PATH = "src/main/resources/config/keys/key.private";
    private static final String PUBLIC_KEY_PATH = "src/main/resources/config/keys/key.pub";
    private static final String RECEIVER = "receiver_key_pair";
    private static final String SENDER = "sender_key_pair";
    private static final char[] PASSWORD_FOR_KEYS = "password".toCharArray();

    public String createDigitalSignature(MultipartFile file) throws Exception {
        Signature signature = Signature.getInstance(CRYPT_ALGORITHM);
        signature.initSign(getPrivateKey());
        byte[] messageBytes = file.getBytes();
        signature.update(messageBytes);
        return encodeSignature(signature.sign());
    }

    public boolean verifyingSignature(MultipartFile file, String digitalSignature) throws Exception {
        Signature signature = Signature.getInstance(CRYPT_ALGORITHM);
        signature.initVerify(getPublicKey());
        byte[] messageBytes = file.getBytes();
        signature.update(messageBytes);
        return signature.verify(decodeSignature(digitalSignature));
    }

    private static PrivateKey getPrivateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEY_FORMAT);
        keyStore.load(new FileInputStream(PRIVATE_KEY_PATH), PASSWORD_FOR_KEYS);
        return (PrivateKey) keyStore.getKey(RECEIVER, PASSWORD_FOR_KEYS);
    }

    private static PublicKey getPublicKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEY_FORMAT);
        keyStore.load(new FileInputStream(PUBLIC_KEY_PATH), PASSWORD_FOR_KEYS);
        Certificate certificate = keyStore.getCertificate(SENDER);
        return certificate.getPublicKey();
    }

    private String encodeSignature(byte[] digitalSignature){
        return Base64.getMimeEncoder().encodeToString(digitalSignature);
    }
    private byte[] decodeSignature(String encodedSignature) {
        return Base64.getMimeDecoder().decode(encodedSignature);
    }
}
