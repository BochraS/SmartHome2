package com.zsoft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SmartObject entity.
 */
public class SmartObjectDTO implements Serializable {

    private Long id;

    private String type;

    private Boolean shared;

    private String fabriquant;

    private String yearOfFabriquation;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String getFabriquant() {
        return fabriquant;
    }

    public void setFabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
    }

    public String getYearOfFabriquation() {
        return yearOfFabriquation;
    }

    public void setYearOfFabriquation(String yearOfFabriquation) {
        this.yearOfFabriquation = yearOfFabriquation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmartObjectDTO smartObjectDTO = (SmartObjectDTO) o;
        if (smartObjectDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smartObjectDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmartObjectDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", shared='" + isShared() + "'" +
            ", fabriquant='" + getFabriquant() + "'" +
            ", yearOfFabriquation='" + getYearOfFabriquation() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
