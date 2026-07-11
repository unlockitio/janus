package org.example.s3learning.storage;

import org.example.s3learning.kubo.kuboservice;
import org.example.s3learning.pinata.pinataservice;
import org.example.s3learning.s3.s3service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class retryservice {
    private final filerecordrepo filerecordrepo;
    private final s3service s3service;
    private final pinataservice pinataservice;
    private final kuboservice  kuboservice;
    public retryservice(filerecordrepo filerecordrepo, s3service s3service,  pinataservice pinataservice,  kuboservice kuboservice) {
        this.filerecordrepo = filerecordrepo;
        this.s3service = s3service;
        this.pinataservice = pinataservice;
        this.kuboservice = kuboservice;
    }
    @Scheduled(fixedRate = 300000)
    public void retryfailedget() {
        List<filerecordnentity>methodtoretry=filerecordrepo.findbystatusAndretrynumbersLessThan("failed",5);
for (filerecordnentity retryfiles: methodtoretry) {
String service=retryfiles.getbackendname();
String bucketname=retryfiles.getbucketname();
try {
    String url = null;
    if (service.equals("s3")) {
        url = s3service.getfileurl(retryfiles.getfilename(), bucketname);
    }
    if (service.equals("pinata")) {
        url = pinataservice.getfileurl(retryfiles.getcid(), null);
    }
    if (service.equals("kubo")){
        url = kuboservice.getfileurl(retryfiles.getcid(), null);
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
