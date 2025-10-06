package com.becker.freelance.component.prediction.gateway.api.dto;

import java.util.Objects;
import java.util.UUID;

public class AppDto {

    private UUID id;
    private String appName;


    public AppDto() {
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
        AppDto app = (AppDto) o;
        return Objects.equals(id, app.id) && Objects.equals(appName, app.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appName);
    }

    @Override
    public String toString() {
        return "AppDto{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                '}';
    }
}
