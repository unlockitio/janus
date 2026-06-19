package org.example.s3learning;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class orgentity {
    @Id
    @GeneratedValue
    private int id;
    private String name;
private Long parentId;

public Long  getParentId() {
	return parentId;
}
public void setParentId(Long parentId) {}
public String getName(){
    return name;
}
public void setName(String name){
    this.name=name;
}
public int getId(){
    return id;
}
}
