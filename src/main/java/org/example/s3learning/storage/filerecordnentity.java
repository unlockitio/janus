package org.example.s3learning.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class filerecordnentity {
    @Id
    @GeneratedValue
    private Long id;
    Long orgId;
    String bucketname;
    String filename;
    String cid;
    String service;
    String status;
    String faileduploadorget;
    int retrynumbers;
    Instant lastattemptat;
    public String getbackendname(){
        return service;
    }
    public Long getorid(){
        return orgId;
    }
    public String getbucketname(){
        return bucketname;
    }
    public String getfilename(){
        return filename;
    }
    public String getcid(){
        return cid;
    }
    public void seturl(String url){
        cid=url;
    }
    public void setstatus(String status){
        this.status=status;
    }
    public int getretrynumbers(){
        return retrynumbers;
    }
    public void setretrynumbers(int retrynumbers){
        this.retrynumbers=retrynumbers;
    }
    public void setLastattemptat(Instant lastattemptat){
        this.lastattemptat=lastattemptat;
    }
    public void setOrgId(Long orgId){
        this.orgId=orgId;
    }
    public void setservice(String service){
        this.service = service;
    }
    public void setBucketname(String bucketname){
        this.bucketname = bucketname;
    }
    public void setFilename(String filename){
        this.filename = filename;
    }
}

