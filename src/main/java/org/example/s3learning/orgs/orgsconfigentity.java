package org.example.s3learning.orgs;

import jakarta.persistence.*;

@Entity
public class orgsconfigentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orgId;
    private String servicename;
    @Column(length = 1000)
    private String credentials;
    private String bucketname;
    public Long getId() {
        return id;

    }
    public int priority;

    public void setorgId(Long orgId) {
        this.orgId = orgId;
    }
    public void setServicename(String servicename) {
        this.servicename = servicename;
    }
    public String getServicename() {
        return servicename;
    }
    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
    public String getCredentials() {
        return credentials;
    }
    public String getBucketname() {
        return bucketname;
    }
    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }
    public int getPriority(){
        return priority;
    }
    public void setPriority(int priority){
        this.priority=priority;
    }

}
