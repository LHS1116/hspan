package com.hspan.hspan.data.models;

import com.hspan.hspan.data.RootEntity;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
@Entity
public class Share extends RootEntity {
    private Long fromID;
    private Long toID;
    private Long fileID;

    private boolean isPublic;
    private boolean isCanceled;

    public Share() {

    }

    public Share(long id, Long fromID, Long toID, Long fileID, boolean isPublic) {
        super(id);
        this.fromID = fromID;
        this.toID = toID;
        this.fileID = fileID;
        this.isCanceled = false;
        this.isPublic = isPublic;
    }

    public void canceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void setToID(Long toID) {
        this.toID = toID;
    }

}
