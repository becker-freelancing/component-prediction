package com.becker.freelance.component.prediction.storage.domain;

import java.math.BigInteger;
import java.util.Objects;

public class App {

    private BigInteger id;
    private String appName;

    public App(BigInteger id, String appName) {
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
