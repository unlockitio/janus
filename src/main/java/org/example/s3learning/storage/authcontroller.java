package org.example.s3learning.storage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RestController
public class authcontroller {
    private authinterface authinterface;
    private PrivateKey apiPrivateKey;

    public authcontroller(authinterface authinterface) throws Exception {
        this.authinterface = authinterface;

        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair pair = gen.generateKeyPair();
        apiPrivateKey = pair.getPrivate();
    }

    @PostMapping("verify")
    public boolean verify(@RequestParam String requestid, @RequestParam String proof, @RequestParam String expecteddocid) throws Exception {

        authentity authentity = authinterface.findById(requestid).orElseThrow();

        String publickey = authentity.getPublicid();
        PublicKey publickeybits = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publickey)));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publickeybits);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(proof));
        return new String(decrypted).equals(expecteddocid);
    }

    @PostMapping("/getFile")
    public byte[] getFile(@RequestParam String requestid, @RequestParam String proof, @RequestParam String expecteddocid) throws Exception {

        boolean verified = verify(requestid, proof, expecteddocid);
        if (!verified) {
              throw new RuntimeException("Not verified");}

           byte[] fileBytes = Files.readAllBytes(Path.of("fileuploads/" + expecteddocid));

        Cipher cipher = Cipher.getInstance("RSA");
           cipher.init(Cipher.ENCRYPT_MODE, apiPrivateKey);
        return cipher.doFinal(fileBytes);
    }
}