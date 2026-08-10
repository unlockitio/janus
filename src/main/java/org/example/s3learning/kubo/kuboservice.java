package org.example.s3learning.kubo;

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
public class kuboservice {
   private final kuboconfig kuboconfig;
    public kuboservice(kuboconfig kuboconfig) {
        this.kuboconfig = kuboconfig;
    }
    public String upload(MultipartFile file,String jwt) throws IOException {
        WebClient kuboclient= kuboconfig.kubowebclient(jwt);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                }
        );
        return kuboclient.post().uri("/api/v0/add").contentType(MediaType.MULTIPART_FORM_DATA).bodyValue(body).retrieve().bodyToMono(HashMap.class).map(response->(String) response.get("Hash")).block();
    }
    public void delete(String cid,String nodeaddress) throws IOException {
        WebClient kuboclient= kuboconfig.kubowebclient(nodeaddress);
        kuboclient.post().uri("/api/v0/pin/rm?arg="+cid).retrieve().bodyToMono(Void.class).block();
    }
    public Map list(String npdeaddress){
        WebClient kuboclient= kuboconfig.kubowebclient(npdeaddress);
        return kuboclient.post().uri("/api/v0/pin/ls").retrieve().bodyToMono(HashMap.class).block();

    }
    public String getfileurl(String nodeaddress,String cid){
        return nodeaddress + cid;
    }


}
