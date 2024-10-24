package com.example.demo_oracle_db.config.authen.dto;

import com.example.demo_oracle_db.entity.Permission;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PermissionInfo {
    // Getters
    private final String name;
    private final String beEndPoint;

    public PermissionInfo(Permission permission){
        this.name = permission.getName();
        this.beEndPoint = permission.getBeEndPoint();
    }

    @JsonCreator
    public PermissionInfo(@JsonProperty("name") String name, @JsonProperty("beEndPoint") String beEndPoint) {
        this.name = name;
        this.beEndPoint = beEndPoint;
    }
}

