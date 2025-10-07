package com.becker.freelance.component.prediction.storage.domain;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class DocumentMetadata {

    private UUID id;
    private App app;
    private String inAppActionPath;
    private String actionTitle;
    private String actionDescription;
    private String actionShortDescription;
    private String locale;
    private BigInteger version;
    private ZonedDateTime createdAt;
    private Set<Tag> tags;

    public DocumentMetadata() {
    }

    public DocumentMetadata(UUID id, App app, String inAppActionPath, String actionTitle, String actionDescription, String actionShortDescription, String locale, BigInteger version, ZonedDateTime createdAt, Set<Tag> tags) {
        this.id = id;
        this.app = app;
        this.inAppActionPath = inAppActionPath;
        this.actionTitle = actionTitle;
        this.actionDescription = actionDescription;
        this.actionShortDescription = actionShortDescription;
        this.locale = locale;
        this.version = version;
        this.createdAt = createdAt;
        this.tags = tags;
    }

    public DocumentMetadata(App app, String inAppActionPath, String actionTitle, String actionDescription, String actionShortDescription, String locale, BigInteger version, ZonedDateTime createdAt, Set<Tag> tags) {
        this(
                null,
                app,
                inAppActionPath,
                actionTitle,
                actionDescription,
                actionShortDescription,
                locale,
                version,
                createdAt,
                tags
        );
    }

    public String getActionShortDescription() {
        return actionShortDescription;
    }

    public void setActionShortDescription(String actionShortDescription) {
        this.actionShortDescription = actionShortDescription;
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

    public String getInAppActionPath() {
        return inAppActionPath;
    }

    public void setInAppActionPath(String inAppActionPath) {
        this.inAppActionPath = inAppActionPath;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentMetadata that = (DocumentMetadata) o;
        return Objects.equals(id, that.id) && Objects.equals(app, that.app) && Objects.equals(inAppActionPath, that.inAppActionPath) && Objects.equals(actionTitle, that.actionTitle) && Objects.equals(actionDescription, that.actionDescription) && Objects.equals(actionShortDescription, that.actionShortDescription) && Objects.equals(locale, that.locale) && Objects.equals(version, that.version) && Objects.equals(createdAt, that.createdAt) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, inAppActionPath, actionTitle, actionDescription, actionShortDescription, locale, version, createdAt, tags);
    }

    @Override
    public String toString() {
        return "DocumentMetadata{" +
                "id=" + id +
                ", app=" + app +
                ", inAppActionPath='" + inAppActionPath + '\'' +
                ", actionTitle='" + actionTitle + '\'' +
                ", actionDescription='" + actionDescription + '\'' +
                ", actionShortDescription='" + actionShortDescription + '\'' +
                ", locale='" + locale + '\'' +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", tags=" + tags +
                '}';
    }
}
