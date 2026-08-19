package org.example.s3learning.storage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.ObjectReadContext;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RestController
public class authcontroller {
private authinterface authinterface;
public authcontroller(authinterface authinterface){
    this.authinterface = authinterface;
}
    @PostMapping("verify")
    public boolean verify(@RequestParam String requestid, @RequestParam String proof, @RequestParam String expecteddocid) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

authentity authentity = authinterface.findById(requestid).orElseThrow();

String publickey = authentity.getPublicid();
PublicKey publickeybits = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publickey)));
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE,publickeybits);
byte[] decrypted=   cipher.doFinal(Base64.getDecoder().decode(proof));
return new String (decrypted).equals(expecteddocid);


    }
}
