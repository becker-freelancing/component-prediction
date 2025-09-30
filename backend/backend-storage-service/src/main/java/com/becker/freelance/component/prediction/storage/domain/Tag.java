package com.becker.freelance.component.prediction.storage.domain;

import java.math.BigInteger;
import java.util.Objects;

public class Tag {

    private BigInteger id;
    private String tag;

    public Tag(String tag) {
        this(null,
                tag);
    }

    public Tag(BigInteger id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public Tag() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(id, tag1.id) && Objects.equals(tag, tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}
