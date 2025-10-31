package com.becker.freelance.component.prediction.storage.domain;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.*;

public class SourceMetadata {

    private UUID id;
    private App app;
    private Locale locale;
    private BigInteger version;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
    private Set<Tag> tags;
    private String fileName;
    private SourceMetadata parent;
    private boolean hasChildren;

    public SourceMetadata() {
    }

    public SourceMetadata(UUID id, App app, Locale locale, BigInteger version, ZonedDateTime createdAt, ZonedDateTime lastModifiedAt, Set<Tag> tags) {
        this.id = id;
        this.app = app;
        this.locale = locale;
        this.version = version;
        this.createdAt = createdAt;
        this.tags = tags;
        this.lastModifiedAt = lastModifiedAt;
    }

    public SourceMetadata(App app, Locale locale, BigInteger version, ZonedDateTime createdAt, ZonedDateTime lastModifiedAt, Set<Tag> tags) {
        this(
                null,
                app,
                locale,
                version,
                createdAt,
                lastModifiedAt,
                tags
        );
    }

    public SourceMetadata(UUID id, App app, BigInteger version, ZonedDateTime createdAt, ZonedDateTime lastModifiedAt, Set<Tag> tags) {
        this(
                id,
                app,
                null,
                version,
                createdAt,
                lastModifiedAt,
                tags
        );
    }

    public SourceMetadata(UUID id, App app, Locale locale, BigInteger version, ZonedDateTime createdAt, ZonedDateTime lastModifiedAt, Set<Tag> tags, String fileName, SourceMetadata parent, boolean hasChildren) {
        this(
                id, app, locale, version, createdAt, lastModifiedAt, tags);
        this.fileName = fileName;
        this.parent = parent;
        this.hasChildren = hasChildren;
    }

    public Optional<String> getFileName() {
        return Optional.ofNullable(fileName);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Optional<SourceMetadata> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(SourceMetadata parent) {
        this.parent = parent;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean children) {
        this.hasChildren = children;
    }

    public ZonedDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Override
    public String toString() {
        return "SourceMetadata{" +
                "id=" + id +
                ", app=" + app +
                ", locale=" + locale +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SourceMetadata that = (SourceMetadata) o;
        return Objects.equals(id, that.id) && Objects.equals(app, that.app) && Objects.equals(locale, that.locale) && Objects.equals(version, that.version) && Objects.equals(createdAt, that.createdAt) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, locale, version, createdAt, tags);
    }
}