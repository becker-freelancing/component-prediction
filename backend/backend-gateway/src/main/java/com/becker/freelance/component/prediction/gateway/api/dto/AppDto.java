package com.becker.freelance.component.prediction.gateway.api.dto;

import java.math.BigInteger;
import java.util.Objects;

public class AppDto {

    private BigInteger id;
    private String appName;


    public AppDto() {
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
