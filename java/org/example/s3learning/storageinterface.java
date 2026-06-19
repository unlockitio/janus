package org.example.s3learning;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.Map;

public interface storageinterface {
    String upload(MultipartFile file,String bucketname) throws IOException;
    void delete(String cid);
    Map list();
}
