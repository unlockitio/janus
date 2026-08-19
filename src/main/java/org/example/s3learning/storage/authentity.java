package org.example.s3learning.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class authentity {
    @Id
    private String id;
    private String publicid;
public  String getPublicid() {
	return publicid;
}
public void setPublicid(String publicid) {
    this.publicid = publicid;
}
public String getId() {
    return id;
}
public void setId(String id) {
    this.id = id;
}
}
