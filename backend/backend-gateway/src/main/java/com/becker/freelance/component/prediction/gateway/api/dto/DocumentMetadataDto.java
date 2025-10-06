package com.becker.freelance.component.prediction.gateway.api.dto;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class DocumentMetadataDto {

    private UUID id;
    private AppDto app;
    private String inAppActionPath;
    private String actionTitle;
    private String actionDescription;
    private String actionShortDescription;
    private String locale;
    private BigInteger version;
    private ZonedDateTime createdAt;
    private Set<TagDto> tags;

    public DocumentMetadataDto() {
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

    public String getActionShortDescription() {
        return actionShortDescription;
    }

    public void setActionShortDescription(String actionShortDescription) {
        this.actionShortDescription = actionShortDescription;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentMetadataDto that = (DocumentMetadataDto) o;
        return Objects.equals(id, that.id) && Objects.equals(app, that.app) && Objects.equals(inAppActionPath, that.inAppActionPath) && Objects.equals(actionTitle, that.actionTitle) && Objects.equals(actionDescription, that.actionDescription) && Objects.equals(actionShortDescription, that.actionShortDescription) && Objects.equals(locale, that.locale) && Objects.equals(version, that.version) && Objects.equals(createdAt, that.createdAt) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, inAppActionPath, actionTitle, actionDescription, actionShortDescription, locale, version, createdAt, tags);
    }

    @Override
    public String toString() {
        return "DocumentMetadataDto{" +
                "id=" + id +
                ", appDto=" + app +
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
