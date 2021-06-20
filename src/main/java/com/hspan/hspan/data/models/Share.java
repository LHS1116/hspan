package com.hspan.hspan.data.models;

import lombok.Getter;

import javax.persistence.Entity;

@Getter
@Entity
public class Share extends RootEntity {
    private Long fromID;
    private Long toID;
    private Long fileID;

    private String code;

    private boolean isPublic;
    private boolean isCanceled;

    public Share() {

    }

    public Share(long id, Long fromID, Long toID, Long fileID, boolean isPublic, String code) {
        super(id);
        this.fromID = fromID;
        this.toID = toID;
        this.fileID = fileID;
        this.isCanceled = false;
        this.isPublic = isPublic;
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void canceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void setToID(Long toID) {
        this.toID = toID;
    }

    @Override
    public String toString() {
        return "Share{" +
                "fromID=" + fromID +
                ", toID=" + toID +
                ", fileID=" + fileID +
                ", code='" + code + '\'' +
                ", isPublic=" + isPublic +
                ", isCanceled=" + isCanceled +
                '}';
    }


}
