package org.example.s3learning.s3;
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
public class s3service  {
   private final s3coonfig s3coonfig;


public s3service(s3coonfig s3coonfig) {

    this.s3coonfig = s3coonfig;

}
    public String upload(MultipartFile file, String bucketname,String accesskey,String secretkey) throws IOException {
    S3Client s3 = s3coonfig.s3client(accesskey,secretkey);
 s3.putObject(PutObjectRequest.builder().bucket(bucketname).key(file.getOriginalFilename()).build(), RequestBody.fromBytes(file.getBytes()));
       return file.getOriginalFilename();
    }
    public Map list(String bucketname, String accesskey,String secretkey) throws IOException {
        S3Client s3 = s3coonfig.s3client(accesskey,secretkey);

        ListObjectsV2Response response = s3.listObjectsV2(

                ListObjectsV2Request.builder().bucket(bucketname).build());

       Map result=new HashMap();
       result.put("fle",response.contents());
       return result;
    }
public void delete(String key,String bucketname, String accesskey,String secretkey) throws IOException {
    S3Client s3 = s3coonfig.s3client(accesskey,secretkey);

    s3.deleteObject(DeleteObjectRequest.builder().bucket(bucketname).key(key).build());
System.out.println("Deleted: " + key);
}

public String getfileurl(String cid,String bucketname, String accesskey,String secretkey) throws IOException {
    S3Presigner s3 = s3coonfig.s3preassigner(accesskey,secretkey);

    GetObjectRequest request= GetObjectRequest.builder().bucket(bucketname).key(cid).build();
    GetObjectPresignRequest presignRequest= GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(15)).getObjectRequest(request).build();
    return s3.presignGetObject(presignRequest).url().toString();
}



}