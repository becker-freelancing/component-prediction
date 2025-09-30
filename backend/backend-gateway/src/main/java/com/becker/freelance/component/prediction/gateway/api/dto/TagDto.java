package com.becker.freelance.component.prediction.gateway.api.dto;

import java.math.BigInteger;
import java.util.Objects;

public class TagDto {


    private BigInteger id;
    private String tag;

    public TagDto() {
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
        TagDto tag1 = (TagDto) o;
        return Objects.equals(id, tag1.id) && Objects.equals(tag, tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}
