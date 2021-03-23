package com.hspan.hspan.data.models;

import com.hspan.hspan.data.RootEntity;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
@Entity
public class Share extends RootEntity {
    private Long fromID;
    private Long toID;

    private boolean isCanceled;

    public Share(long id, Long fromID, Long toID, boolean isCanceled) {
        super(id);
        this.fromID = fromID;
        this.toID = toID;
        this.isCanceled = isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void setToID(Long toID) {
        this.toID = toID;
    }

}
