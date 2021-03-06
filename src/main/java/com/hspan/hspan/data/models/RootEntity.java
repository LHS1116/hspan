package com.hspan.hspan.data.models;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class RootEntity {
    @Id
    private long id;

    @Getter
    private Long createdAt;

    @Getter
    private Long updatedAt;

    public Long getId() {
        return id;
    }

    public RootEntity() {

        createdAt = System.currentTimeMillis();
        updatedAt = createdAt;
    }

    public RootEntity(long id) {
        this.id = id;
        createdAt = System.currentTimeMillis();
        updatedAt = createdAt;
    }

    public void updatedAtNow() {
        updatedAt = System.currentTimeMillis();
    }

}
