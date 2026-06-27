package org.example.s3learning;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class orgservice {
    private final orgsrep orgsrep;
    private final orgconfig orgconfig;
    public orgservice(orgsrep orgsrep, orgconfig orgconfig) {
        this.orgsrep = orgsrep;
        this.orgconfig = orgconfig;
    }
    public orgentity createorg(orgentity orgentity) {
        return orgsrep.save(orgentity);
    }
    public void  deleteorg(int id) {
        orgsrep.deleteById(id);
    }
    public void  updateorg(orgentity orgentity) {
        orgsrep.save(orgentity);
    }
    public List<orgentity> listorgs(){
        return orgsrep.findAll();
    }
    public orgsconfigentity configcreate(Long id,orgsconfigentity orgsconfigentity){
orgsconfigentity.setorgId(id);
        return orgconfig.save(orgsconfigentity);
    }


}
