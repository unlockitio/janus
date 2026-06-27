package org.example.s3learning.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface storageinterface {
    String upload(MultipartFile file,String bucketname) throws IOException;
    void delete(String cid,String bucketname);
    Map list(String bucketname);
}
