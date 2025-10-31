package com.becker.freelance.component.prediction.storage.adapter.jpa;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Table
@Entity(name = "source_metadata")
public class SourceMetadataEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private AppsEntity app;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "metadata_tags",
            joinColumns = @JoinColumn(name = "metadata_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagsEntity> tags;

    @Column(name = "locale", nullable = false, length = 2)
    private String locale;

    @Column(name = "version", nullable = false)
    private BigInteger version;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    private ZonedDateTime lastModifiedAt;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SourceMetadataEntity parent;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AppsEntity getApp() {
        return app;
    }

    public void setApp(AppsEntity app) {
        this.app = app;
    }

    public Set<TagsEntity> getTags() {
        return tags;
    }

    public void setTags(Set<TagsEntity> tags) {
        this.tags = tags;
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

    public SourceMetadataEntity getParent() {
        return parent;
    }

    public void setParent(SourceMetadataEntity parent) {
        this.parent = parent;
    }
}
