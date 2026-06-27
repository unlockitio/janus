package org.example.s3learning.s3;

import org.example.s3learning.storage.storageinterface;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;


import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class s3service implements storageinterface {
    private final S3Presigner  s3Presigner;
    private final S3Client S3Client;


public s3service(S3Client S3Client,S3Presigner s3Presigner){

    this.S3Client=S3Client;
    this.s3Presigner=s3Presigner;

}
    public String upload(MultipartFile file, String bucketname) throws IOException {
 S3Client.putObject(PutObjectRequest.builder().bucket(bucketname).key(file.getOriginalFilename()).build(), RequestBody.fromBytes(file.getBytes()));
       return file.getOriginalFilename();
    }
    public Map list(String bucketname) {
        ListObjectsV2Response response = S3Client.listObjectsV2(

                ListObjectsV2Request.builder().bucket(bucketname).build());

       Map result=new HashMap();
       result.put("fle",response.contents());
       return result;
    }
public void delete(String key,String bucketname){
    S3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketname).key(key).build());
System.out.println("Deleted: " + key);
}

public String getfileurl(String key,String bucketname){
GetObjectRequest request= GetObjectRequest.builder().bucket(bucketname).key(key).build();
    GetObjectPresignRequest presignRequest= GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(15)).getObjectRequest(request).build();
    return s3Presigner.presignGetObject(presignRequest).url().toString();
}



}