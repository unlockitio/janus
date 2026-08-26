package org.example.s3learning.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class srvrkeyentity {
    @Id
    private Long id = 1L;
    private String privateKeyBase64;
    private String publicKeyBase64;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPrivateKeyBase64() { return privateKeyBase64; }
    public void setPrivateKeyBase64(String v) { this.privateKeyBase64 = v; }
    public String getPublicKeyBase64() { return publicKeyBase64; }
    public void setPublicKeyBase64(String v) { this.publicKeyBase64 = v; }
}