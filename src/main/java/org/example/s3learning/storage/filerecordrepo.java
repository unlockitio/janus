package org.example.s3learning.storage;

import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface filerecordrepo extends JpaRepository<filerecordnentity,Long> {
    List<filerecordnentity>findbystatusAndretrynumbersLessThan(String status,int retrynumbers);
}
