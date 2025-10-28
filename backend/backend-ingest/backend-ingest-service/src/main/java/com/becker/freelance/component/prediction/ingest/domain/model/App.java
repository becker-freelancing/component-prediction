package com.becker.freelance.component.prediction.ingest.domain.model;

import java.util.Objects;
import java.util.UUID;

public class App {

    private UUID id;
    private String appName;

    public App(UUID id, String appName) {
        this.id = id;
        this.appName = appName;
    }

    public App(String appName) {
        this(
                null,
                appName
        );
    }

    public App() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return Objects.equals(id, app.id) && Objects.equals(appName, app.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appName);
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                '}';
    }
}
