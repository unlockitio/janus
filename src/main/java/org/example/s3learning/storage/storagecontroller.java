package org.example.s3learning.storage;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
public class storagecontroller {
    private final storageservice storageservice;
    public storagecontroller(storageservice storageservice) {
        this.storageservice = storageservice;
    }
    @PostMapping("/upload/{Id}")
    public void upload(@PathVariable Long Id, @RequestParam MultipartFile file) throws IOException {
        String path=storageservice.savelocally(file);
        storageservice.upload(Id,path);

    }
    @DeleteMapping("/delete/{orgId}/{id}")
    public String delete(@PathVariable String id, @PathVariable Long orgId)  {

        storageservice.delete( orgId,String.valueOf(id));
        return "done deleting";
    }
    @GetMapping("/list/{orgId}")
    public List<Map> list(@PathVariable Long orgId) {
        return storageservice.getList(orgId);
    }


}
