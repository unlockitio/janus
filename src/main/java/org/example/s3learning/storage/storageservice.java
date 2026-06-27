package org.example.s3learning.storage;

import org.example.s3learning.kubo.kuboservice;
import org.example.s3learning.orgs.orgconfig;
import org.example.s3learning.orgs.orgentity;
import org.example.s3learning.orgs.orgsconfigentity;
import org.example.s3learning.orgs.orgsrep;
import org.example.s3learning.pinata.pinataservice;
import org.example.s3learning.s3.s3service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class storageservice {
    private final orgconfig orgconfig;
    private final s3service s3service;
    private final pinataservice pinataservice;
    private final orgsrep orgsrep;
    private final kuboservice kuboservice;


    public storageservice(orgsrep orgsrep,orgconfig orgconfig, s3service s3service, pinataservice pinataservice,  kuboservice kuboservice) {
        this.orgconfig = orgconfig;
        this.s3service = s3service;
        this.pinataservice = pinataservice;
        this.orgsrep = orgsrep;
        this.kuboservice = kuboservice;
    }

    public List<String> upload(Long orgId,MultipartFile file) throws IOException {
        List<String> results = new ArrayList<>();
        List<orgsconfigentity>providers=orgconfig.findByOrgId(orgId);

        for (orgsconfigentity services : providers){
            String provider=services.getServicename();
            if (provider.equals("s3")){
                results.add(s3service.upload(file,services.getBucketname()));
            }
            if(provider.equals("ipfs")){
                results.add(pinataservice.upload(file,null));
            }
            if(provider.equals("kubo")){
                results.add(kuboservice.upload(file,null));
            }

        }
        List<orgentity>children=orgsrep.findByParentId(orgId);
        for(orgentity childorg : children){
            List<orgsconfigentity>childproviders=orgconfig.findByOrgId((long) childorg.getId());
            for (orgsconfigentity childservice : childproviders){
                String provider=childservice.getServicename();
                if (provider.equals("s3")){
                    results.add("child propagation s3"+s3service.upload(file,childservice.getBucketname()));
                }
                if(provider.equals("ipfs")){
                    results.add("child propagation ipfs"+pinataservice.upload(file,null));
                }
                if(provider.equals("kubo")){
                    results.add(kuboservice.upload(file,null));
                }

            }
        }
        return results;
    }
    public void delete(Long id, String todeleteid){
        for(orgsconfigentity services: orgconfig.findByOrgId(id)){
            String provider=services.getServicename();
            if (provider.equals("s3")){
                s3service.delete(todeleteid, services.getBucketname());
            }
            if(provider.equals("ipfs")){
                pinataservice.delete(todeleteid, null);
            }
            if(provider.equals("kubo")){
                kuboservice.delete(todeleteid, null);
            }
        }
    }
    public List<Map> getList(Long orgId){
        List<Map> list = new ArrayList<>();
        for (orgsconfigentity services: orgconfig.findByOrgId(orgId)){
            String provider=services.getServicename();
            if (provider.equals("s3")){
                list.add(s3service.list(services.getBucketname()));
            }
            if(provider.equals("ipfs")){
                list.add(pinataservice.list(null));
            }
            if (provider.equals("kubo")){
                list.add(kuboservice.list(null));
            }

        }
        return list;
    }
}
