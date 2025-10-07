package com.becker.freelance.component.prediction.storage.adapter;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Table
@Entity(name = "document_metadata")
public class DocumentMetadataEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    private AppsEntity app;

    @ManyToMany
    @JoinTable(
            name = "document_tags",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagsEntity> tags;

    @Column(name = "in_app_action_path", nullable = false, length = Integer.MAX_VALUE)
    private String inAppActionPath;

    @Column(name = "action_title", nullable = false, length = Integer.MAX_VALUE)
    private String actionTitle;

    @Column(name = "action_description", nullable = false, length = Integer.MAX_VALUE)
    private String actionDescription;

    @Column(name = "action_short_description", nullable = false, length = Integer.MAX_VALUE)
    private String actionShortDescription;

    @Column(name = "locale", nullable = false, length = 2)
    private String locale;

    @Column(name = "version", nullable = false)
    private BigInteger version;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

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
}
