package org.example.s3learning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface orgsrep extends JpaRepository<orgentity, Integer> {
    List<orgentity> findByParentId(Long id);
}
