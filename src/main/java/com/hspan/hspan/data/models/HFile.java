package com.hspan.hspan.data.models;

import com.hspan.hspan.data.RootEntity;
import lombok.Getter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class HFile extends RootEntity {

    private String filePath;

    @ElementCollection(targetClass=Long.class)
    private List<Long> owners;

    private Long size;

    private Access access;

    public HFile() {

    }

    public HFile(long id, String filePath, Long size, Access access) {
        super(id);
        this.filePath = filePath;
        this.size = size;
        this.access = access;
        this.owners = new ArrayList<>();
    }

    public void addOwner(Long ownerId) {
        owners.add(ownerId);
    }

    public void removeOwner(Long ownerId) {
        owners.remove(ownerId);
    }
}
