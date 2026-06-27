//package org.example.s3learning;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//
//@RestController
//public class pinatacontroller {
//    private final pinataservice pinataservice;
//    pinatacontroller(pinataservice pinataservice) {
//        this.pinataservice = pinataservice;
//    }
//    @PostMapping ("/ipfs/   upload")
//    public String pinataupload (@RequestParam ("file") MultipartFile file) throws IOException {
//        return pinataservice.pinupload(file);
//
//    }
//    @DeleteMapping("/ipfs/delete/{cid}")
//    public void pinatadelete(@PathVariable String cid) {
//        pinataservice.pindelete(cid);
//    }
//    @GetMapping("/ipfs/list")
//    public Map pinatalist(){
//        return pinataservice.pinlist();
//    }
//}
