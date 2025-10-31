package com.becker.freelance.component.prediction.gateway.api.dto;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SourceMetadataDto {

    private UUID id;
    private AppDto app;
    private String locale;
    private BigInteger version;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
    private Set<TagDto> tags;
    private String fileName;
    private SourceMetadataDto parent;
    private boolean hasChildren;


    public SourceMetadataDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AppDto getApp() {
        return app;
    }

    public void setApp(AppDto app) {
        this.app = app;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    public ZonedDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SourceMetadataDto getParent() {
        return parent;
    }

    public void setParent(SourceMetadataDto parent) {
        this.parent = parent;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SourceMetadataDto that = (SourceMetadataDto) o;
        return Objects.equals(id, that.id) && Objects.equals(app, that.app) && Objects.equals(locale, that.locale) && Objects.equals(version, that.version) && Objects.equals(createdAt, that.createdAt) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, locale, version, createdAt, tags);
    }

    @Override
    public String toString() {
        return "SourceMetadataDto{" +
                "id=" + id +
                ", app=" + app +
                ", locale='" + locale + '\'' +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", tags=" + tags +
                '}';
    }
}
