package org.example.s3learning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class pinataservice implements storageinterface {
    private final String jwt;
    private final WebClient webClient;

    public pinataservice(@Value("${jwt}") String jwt, WebClient webClient) {
        this.jwt = jwt;
        this.webClient = webClient;


    }

    public String upload(MultipartFile file,String bucketname) throws IOException {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
return webClient.post().uri("/pinning/pinFileToIPFS").contentType(MediaType.MULTIPART_FORM_DATA).bodyValue(body).retrieve().bodyToMono(HashMap.class).map(response->(String) response.get("IpfsHash")).block();
    }
    public void delete(String cid,String bucketname)  {
webClient.delete().uri("/pinning/unpin/"+cid).retrieve().bodyToMono(Void.class).block();

    }

public Map list(String bucketname){
    return webClient.get().uri("/data/pinList").retrieve().bodyToMono(HashMap.class).block();
}}