package com.becker.freelance.component.prediction.gateway.api;

import java.util.List;
import java.util.Objects;

public final class DocumentDto {

    private String documentId;
    private String appName;
    private String inAppActionPath;
    private String actionTitle;
    private String actionDescription;
    private String actionShortDescription;
    private List<String> tags;
    private String locale;
    private String version;
    private String creator;
    private String createdAt;

    public DocumentDto(String documentId, String appName, String inAppActionPath, String actionTitle, String actionDescription, String actionShortDescription, List<String> tags, String locale, String version, String creator, String createdAt) {
        this.documentId = documentId;
        this.appName = appName;
        this.inAppActionPath = inAppActionPath;
        this.actionTitle = actionTitle;
        this.actionDescription = actionDescription;
        this.actionShortDescription = actionShortDescription;
        this.tags = tags;
        this.locale = locale;
        this.version = version;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getAppName() {
        return appName;
    }

    public String getInAppActionPath() {
        return inAppActionPath;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public String getActionShortDescription() {
        return actionShortDescription;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getLocale() {
        return locale;
    }

    public String getVersion() {
        return version;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentDto that = (DocumentDto) o;
        return Objects.equals(documentId, that.documentId) && Objects.equals(appName, that.appName) && Objects.equals(inAppActionPath, that.inAppActionPath) && Objects.equals(actionTitle, that.actionTitle) && Objects.equals(actionDescription, that.actionDescription) && Objects.equals(actionShortDescription, that.actionShortDescription) && Objects.equals(tags, that.tags) && Objects.equals(locale, that.locale) && Objects.equals(version, that.version) && Objects.equals(creator, that.creator) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, appName, inAppActionPath, actionTitle, actionDescription, actionShortDescription, tags, locale, version, creator, createdAt);
    }
}
