package com.becker.freelance.component.prediction.storage.adapter;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tags")
public class TagsEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "tag", nullable = false, length = 1024)
    private String tag;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
