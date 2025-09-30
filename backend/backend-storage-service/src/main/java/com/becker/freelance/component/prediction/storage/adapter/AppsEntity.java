package com.becker.freelance.component.prediction.storage.adapter;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "apps")
public class AppsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "app_name", nullable = false, length = 1024)
    private String appName;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentMetadataEntity> metadata;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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
