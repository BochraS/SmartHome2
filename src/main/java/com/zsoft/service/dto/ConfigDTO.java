package com.zsoft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Config entity.
 */
public class ConfigDTO implements Serializable {

    private Long id;

    private String key;

    private String description;

    private String des;

    private Long smartObjectId;

    private Long profileConfValueId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Long getSmartObjectId() {
        return smartObjectId;
    }

    public void setSmartObjectId(Long smartObjectId) {
        this.smartObjectId = smartObjectId;
    }

    public Long getProfileConfValueId() {
        return profileConfValueId;
    }

    public void setProfileConfValueId(Long profileConfValueId) {
        this.profileConfValueId = profileConfValueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigDTO configDTO = (ConfigDTO) o;
        if (configDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", description='" + getDescription() + "'" +
            ", des='" + getDes() + "'" +
            ", smartObject=" + getSmartObjectId() +
            ", profileConfValue=" + getProfileConfValueId() +
            "}";
    }
}
