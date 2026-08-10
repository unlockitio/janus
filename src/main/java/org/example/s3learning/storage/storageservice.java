package org.example.s3learning.storage;

import org.example.s3learning.kubo.kuboconfig;
import org.example.s3learning.kubo.kuboservice;
import org.example.s3learning.orgs.orgconfig;
import org.example.s3learning.orgs.orgentity;
import org.example.s3learning.orgs.orgsconfigentity;
import org.example.s3learning.orgs.orgsrep;
import org.example.s3learning.pinata.pinataservice;
import org.example.s3learning.s3.s3service;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class storageservice {
    private final orgconfig orgconfig;
    private final s3service s3service;
    private final pinataservice pinataservice;
    private final orgsrep orgsrep;
    private final kuboservice kuboservice;
    private final filerecordrepo filerecordrepo;


    public storageservice(orgsrep orgsrep,orgconfig orgconfig, s3service s3service, pinataservice pinataservice,  kuboservice kuboservice, filerecordrepo filerecordrepo) {
        this.orgconfig = orgconfig;
        this.s3service = s3service;
        this.pinataservice = pinataservice;
        this.orgsrep = orgsrep;
        this.kuboservice = kuboservice;
        this.filerecordrepo = filerecordrepo;
    }
public String savelocally(MultipartFile file) throws IOException {
        String tempid= UUID.randomUUID().toString();
        String name = file.getOriginalFilename()+"_"+tempid;
Path path= Paths.get("fileuploads");
Files.createDirectories(path);
Path donepath=path.resolve(name);
file.transferTo(donepath);
return donepath.toString();
}
@Async
    public void upload(Long orgId, String path) throws IOException {
        List<String> results = new ArrayList<>();
    File diskFile = new File(path);
    MultipartFile file = new MockMultipartFile(
            diskFile.getName(),
            diskFile.getName(),
            "application/octet-stream",
            Files.readAllBytes(diskFile.toPath())
    );
        List<orgsconfigentity>providers=orgconfig.findByOrgId(orgId);

        for (orgsconfigentity services : providers){
            String provider=services.getServicename();
            filerecordnentity record=new filerecordnentity();
            record.setOrgId(orgId);
            record.setBucketname(services.getBucketname());
            record.setFilename(path);
            record.setservice(provider);
            record.setstatus("PENDING");
            record.setretrynumbers(0);
            record.setLastattemptat(Instant.now());
            filerecordrepo.save(record);
            if (provider.equals("s3")){
                try {
                    String[] keys= services.getCredentials().split(",");
                    String result = s3service.upload(file, services.getBucketname(),keys[0],keys[1]);
                    record.seturl(result);
                    record.setstatus("SUCCESS");
                    results.add(result);
                } catch (Exception e) {
                    record.setstatus("FAILED");
                }
                filerecordrepo.save(record);
            }
            if (provider.equals("ipfs")){
                try {
                    String credentials= services.getCredentials();
                    String result = pinataservice.upload(file, credentials);
                    record.seturl(result);
                    record.setstatus("SUCCESS");
                    results.add(result);
                } catch (Exception e) {
                    record.setstatus("FAILED");
                }
                filerecordrepo.save(record);
            }
            if (provider.equals("kubo")){
                try {
                    String result = kuboservice.upload(file, services.getBucketname());
                    record.seturl(result);
                    record.setstatus("SUCCESS");
                    results.add(result);
                } catch (Exception e) {
                    record.setstatus("FAILED");
                }
                filerecordrepo.save(record);
            }

        }
    List<orgentity>children=orgsrep.findByParentId(orgId);
    for(orgentity childorg : children){
        List<orgsconfigentity>childproviders=orgconfig.findByOrgId((long) childorg.getId());
        for (orgsconfigentity childservice : childproviders){
            String provider=childservice.getServicename();
            filerecordnentity childRecord=new filerecordnentity();
            childRecord.setOrgId((long) childorg.getId());
            childRecord.setBucketname(childservice.getBucketname());
            childRecord.setFilename(path);
            childRecord.setservice(provider);
            childRecord.setstatus("PENDING");
            childRecord.setretrynumbers(0);
            childRecord.setLastattemptat(Instant.now());
            filerecordrepo.save(childRecord);

            if (provider.equals("s3")){
                try {
                    String[] keys= childservice.getCredentials().split(",");
                    String result = s3service.upload(file, childservice.getBucketname(),keys[0],keys[1]);
                    childRecord.seturl(result);
                    childRecord.setstatus("SUCCESS");
                    results.add("child propagation s3 "+result);
                } catch (Exception e) {
                    childRecord.setstatus("FAILED");
                }
                filerecordrepo.save(childRecord);
            }
            if (provider.equals("ipfs")){
                try {
                    String credentials= childservice.getCredentials();
                    String result = pinataservice.upload(file, childservice.getCredentials());
                    childRecord.seturl(result);
                    childRecord.setstatus("SUCCESS");
                    results.add("child propagation ipfs "+result);
                } catch (Exception e) {
                    childRecord.setstatus("FAILED");
                }
                filerecordrepo.save(childRecord);
            }
            if (provider.equals("kubo")){
                try {
                    String result = kuboservice.upload(file, childservice.getBucketname());
                    childRecord.seturl(result);
                    childRecord.setstatus("SUCCESS");
                    results.add("child propagation kubo "+result);
                } catch (Exception e) {
                    childRecord.setstatus("FAILED");
                }
                filerecordrepo.save(childRecord);
            }
        }
    }

    }
    public void delete(Long id, String todeleteid) throws IOException {
        for(orgsconfigentity services: orgconfig.findByOrgId(id)){
            String provider=services.getServicename();
            if (provider.equals("s3")){
                String[] keys = services.getCredentials().split(",");
                s3service.delete(todeleteid, services.getBucketname(),keys[0],keys[1]);

            }
            if(provider.equals("ipfs")){
                String credentials= services.getCredentials();
                pinataservice.delete(todeleteid,credentials );
            }
            if(provider.equals("kubo")){
                kuboservice.delete(todeleteid, services.getBucketname());
            }
        }
    }
    public List<Map> getList(Long orgId) throws IOException {
        List<Map> list = new ArrayList<>();
        for (orgsconfigentity services: orgconfig.findByOrgId(orgId)){
            String provider=services.getServicename();
            if (provider.equals("s3")){
                String[] keys= services.getCredentials().split(",");
                list.add(s3service.list(services.getBucketname(),keys[0],keys[1]));
            }
            if(provider.equals("ipfs")){
                String credentials= services.getCredentials();
                list.add(pinataservice.list(credentials));
            }
            if (provider.equals("kubo")){
                list.add(kuboservice.list(services.getBucketname()));
            }

        }
        return list;
    }
    public String getfileurl(String id,String cid, String bucketname) {
        List<orgsconfigentity> providers = orgconfig.findByOrgId(Long.valueOf(id));
        for (orgsconfigentity services : providers) {
            String provider = services.getServicename();
            if (provider.equals("s3")) {
                try {
                    String[] keys= services.getCredentials().split(",");
                    return s3service.getfileurl(cid, bucketname, keys[0], keys[1]);
                } catch (Exception e) {


                }
            }
            if (provider.equals("ipfs")) {
                try {
                    return pinataservice.getfileurl(cid, null);
                } catch (Exception e) {
                }
            }
            if (provider.equals("kubo")) {
                try {
                    return kuboservice.getfileurl(services.getBucketname(), cid);
                } catch (Exception e) {
                }
            }

        }
        return null;
    }
}
