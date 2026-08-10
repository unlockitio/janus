package org.example.s3learning.storage;

import org.example.s3learning.kubo.kuboservice;
import org.example.s3learning.orgs.orgconfig;
import org.example.s3learning.orgs.orgsconfigentity;
import org.example.s3learning.pinata.pinataservice;
import org.example.s3learning.s3.s3service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Instant;
import java.util.List;

@Component
public class retryservice {
    private final filerecordrepo filerecordrepo;
    private final s3service s3service;
    private final pinataservice pinataservice;
    private final kuboservice  kuboservice;
    private final orgconfig orgconfig;
    public retryservice(filerecordrepo filerecordrepo, s3service s3service,  pinataservice pinataservice,  kuboservice kuboservice, orgconfig orgconfig) {
        this.filerecordrepo = filerecordrepo;
        this.s3service = s3service;
        this.pinataservice = pinataservice;
        this.kuboservice = kuboservice;
        this.orgconfig=orgconfig;
    }
    @Scheduled(fixedRate = 300000)
    public void retryfailedget() {
        List<filerecordnentity>methodtoretry=filerecordrepo.findByStatusAndRetrynumbersLessThan("failed",5);
for (filerecordnentity retryfiles: methodtoretry) {
String service=retryfiles.getbackendname();
String bucketname=retryfiles.getbucketname();
try {
    String url = null;
    if (service.equals("s3")) {
        List<orgsconfigentity> configs = orgconfig.findByOrgId(retryfiles.getorid());
        for (orgsconfigentity c : configs) {
            if (c.getServicename().equals("s3") && c.getBucketname().equals(bucketname)) {
                String[] keys = c.getCredentials().split(",");
                url = s3service.getfileurl(retryfiles.getcid(), bucketname, keys[0], keys[1]);
            }
        }
    }
    if (service.equals("pinata")) {
        url = pinataservice.getfileurl(retryfiles.getcid(), null);
    }
    if (service.equals("kubo")){
        url = kuboservice.getfileurl(bucketname, retryfiles.getcid());
    }

    retryfiles.seturl(url);
    retryfiles.setstatus("success");
}
catch (Exception e) {
    retryfiles.setretrynumbers(retryfiles.getretrynumbers()+1);
}
retryfiles.setLastattemptat(Instant.now());
filerecordrepo.save(retryfiles);
}
    }
}
