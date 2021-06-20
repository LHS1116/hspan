package com.hspan.hspan.data.models;

import lombok.Getter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class HFile extends RootEntity {

    private String filePath;

    private String fileName;

    @ElementCollection
    private List<Long> owners;

    private Long size;

    private Access access;

    public HFile() {

    }

    public HFile(long id, String filePath, Long size, String fileName, Access access) {
        super(id);
        this.filePath = filePath;
        this.fileName = fileName;
        this.size = size;
        this.access = access;
        this.owners = new ArrayList<>();
    }

    public void addOwner(Long ownerId) {
//        if (owners.length() > 0) {
//            owners += ',';
//        }
//        owners  += ownerId;
        owners.add(ownerId);
    }

    public void removeOwner(Long ownerId) {
//        StringBuilder sb = new StringBuilder();
//        String[] ownerArr = owners.split(",");
//        for (String owner:ownerArr) {
//            if (!owner.equals(ownerId.toString())) {
//                sb.append(owner).append(',');
//            }
//        }
//        owners = sb.toString();
        owners.remove(ownerId);
    }
    public int getOwnerCount() {
        return owners.size();
    }

    @Override
    public String toString() {
        return "HFile{" +
                "filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", owners='" + owners + '\'' +
                ", size=" + size +
                ", access=" + access +
                '}';
    }
}
