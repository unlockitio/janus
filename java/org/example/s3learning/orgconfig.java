package org.example.s3learning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface orgconfig extends JpaRepository <orgsconfigentity,Integer> {
    List<orgsconfigentity> findByOrgId(Long id);
}
