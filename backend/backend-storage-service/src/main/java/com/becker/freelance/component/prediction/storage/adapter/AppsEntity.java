package com.becker.freelance.component.prediction.storage.adapter;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "apps")
public class AppsEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "app_name", nullable = false, length = 1024)
    private String appName;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentMetadataEntity> metadata;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<DocumentMetadataEntity> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<DocumentMetadataEntity> metadata) {
        this.metadata = metadata;
    }
}
