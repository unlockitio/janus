package org.example.s3learning;

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


    public storageservice(List<storageinterface> storage, orgconfig orgconfig, s3service s3service, pinataservice pinataservice) {
        this.orgconfig = orgconfig;
        this.s3service = s3service;
        this.pinataservice = pinataservice;
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

        }
        return results;
    }
    public void delete(Long id){
        for(orgsconfigentity services: orgconfig.findByOrgId(id)){
            String provider=services.getServicename();
            if (provider.equals("s3")){
                s3service.delete(String.valueOf(id), services.getBucketname());
            }
            if(provider.equals("ipfs")){
                pinataservice.delete(String.valueOf(id), null);
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
                list.add(pinataservice.list());
            }

        }
        return list;
    }
}
