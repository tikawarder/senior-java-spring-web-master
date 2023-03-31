package hu.ponte.hr.services;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

@Service
public class SignService {
    public static final String CRYPT_ALGORITHM = "SHA256withRSA";
    public static final String KEY_FORMAT = "PKCS8";

    public static PrivateKey getPrivateKey(String file, char[] password, String storeType, String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(storeType);
        keyStore.load(new FileInputStream(file), password);
        return (PrivateKey) keyStore.getKey(alias, password);
    }

    public static PublicKey getPublicKey(String file, char[] password, String storeType, String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(storeType);
        keyStore.load(new FileInputStream(file), password);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate.getPublicKey();
    }

    public byte[] createDigitalSignature(PrivateKey privateKey, URI filePath) throws NoSuchAlgorithmException,
            InvalidKeyException,
            IOException, SignatureException {
        Signature signature = Signature.getInstance(CRYPT_ALGORITHM);
        signature.initSign(privateKey);
        byte[] messageBytes = Files.readAllBytes(Paths.get(filePath));
        signature.update(messageBytes);
        return signature.sign();
    }

    public boolean verifyingSignature(PublicKey publicKey, URI filePath, byte[] digitalSignature) throws NoSuchAlgorithmException,
            InvalidKeyException,
            IOException, SignatureException {
        Signature signature = Signature.getInstance(CRYPT_ALGORITHM);
        signature.initVerify(publicKey);
        byte[] messageBytes = Files.readAllBytes(Paths.get(filePath));
        signature.update(messageBytes);
        return signature.verify(digitalSignature);
    }

    public String encodeSignature(byte[] digitalSignature){
        return Base64.getMimeEncoder().encodeToString(digitalSignature);
    }

    public byte[] decodeSignature(String encodedSignature) {
        return Base64.getMimeDecoder().decode(encodedSignature);
    }
}
