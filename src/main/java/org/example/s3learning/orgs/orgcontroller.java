package org.example.s3learning.orgs;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class orgcontroller {
    private final orgservice orgservice;
    public orgcontroller(orgservice orgservice){
        this.orgservice = orgservice;
    }
    @PostMapping("/org/upload")
    public orgentity upload(@RequestBody orgentity orgentity){
        return orgservice.createorg(orgentity);
    }
    @DeleteMapping("/org/delete/{orgid}")
    public void deleteorg(@PathVariable int orgid){
        orgservice.deleteorg(orgid);
    }
    @GetMapping("/orgs")
    public List<orgentity> getorgs(){
        return orgservice.listorgs();
    }
@PostMapping("/orgs/{orgId}/createconfig")
    public orgsconfigentity createorgconfig(@PathVariable long orgId, @RequestBody orgsconfigentity orgsconfigentity){

        return orgservice.configcreate(orgId,orgsconfigentity);
}
@GetMapping("/configentity/{orgId}")
    public List<orgsconfigentity> getorgconfig(@PathVariable long orgId){
        return orgservice.orgconfiglists(orgId);
}

}
