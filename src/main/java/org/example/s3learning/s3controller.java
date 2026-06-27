//package org.example.s3learning;
//
//import org.springframework.web.bind.annotation.*;
//import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
//
//import java.util.List;
//
//@RestController
//public class s3controller {
//    private final s3service s3service;
//    public s3controller(s3service s3service){
//        this.s3service = s3service;
//    }
//    @PostMapping("/s3upload")
//    public String s3push(@RequestParam String filepath, @RequestParam String key){
//        s3service.s3push(filepath, key);
//        return "done pushing";
//    }
//
//    @DeleteMapping("/s3delete/{key}")
//    public String s3delete(@PathVariable  String key){
//        s3service.delete(key);
//        return "done deleting";
//    }
//@PutMapping("/s3edit/{filepath}/{key}")
//    public String s3edit(@PathVariable String filepath,@PathVariable   String key){
//        s3service.edit(filepath,key);
//        return "done editing";
//    }
//    @GetMapping("/s3list")
//    public List<String> s3list(){
//        return s3service.listbucketstuff();
//    }
//    @GetMapping("/geturl/{key}")
//    public String s3getpresignedurl(@PathVariable String key){
//        return s3service.getfileurl(key);
//    }
//}
