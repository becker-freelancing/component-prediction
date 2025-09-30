package com.becker.freelance.component.prediction.storage.adapter;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "tags")
public class TagsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "tag", nullable = false, length = 1024)
    private String tag;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
