package org.example.s3learning;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class kuboservice implements storageinterface{
   private final WebClient kubowebclient;
    public kuboservice(WebClient kubowebclient) {
        this.kubowebclient = kubowebclient;
    }
    public String upload(MultipartFile file,String name) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                }
        );
        return kubowebclient.post().uri("/api/v0/add").contentType(MediaType.MULTIPART_FORM_DATA).bodyValue(body).retrieve().bodyToMono(HashMap.class).map(response->(String) response.get("Hash")).block();
    }
    public void delete(String cid,String bucketname){
        kubowebclient.post().uri("/api/v0/pin/rm?arg="+cid).retrieve().bodyToMono(Void.class).block();
    }
    public Map list(String bucketname){
        return kubowebclient.post().uri("/api/v0/pin/ls").retrieve().bodyToMono(HashMap.class).block();

    }

}
