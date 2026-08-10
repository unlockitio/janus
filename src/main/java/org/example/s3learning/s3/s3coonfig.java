

package org.example.s3learning.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


@Component
public class s3coonfig {




    public S3Client s3client( String accessKey,String secretKey) {
return S3Client.builder().region(Region.EU_NORTH_1).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))).build();
    }

    public S3Presigner s3preassigner(String accessKey,String secretKey) {
        return S3Presigner.builder().region(Region.EU_NORTH_1).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey))).build();

    }
}
