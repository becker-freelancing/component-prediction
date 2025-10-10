package com.becker.freelance.component.prediction.backend.query.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Tag {

    private UUID id;
    private String tag;

    public Tag(String tag) {
        this(null,
                tag);
    }

    public Tag(UUID id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public Tag() {
    }

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
