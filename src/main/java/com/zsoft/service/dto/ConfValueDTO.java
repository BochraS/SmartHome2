package com.zsoft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ConfValue entity.
 */
public class ConfValueDTO implements Serializable {

    private Long id;

    private String value;

    private Long configId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfValueDTO confValueDTO = (ConfValueDTO) o;
        if (confValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfValueDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", config=" + getConfigId() +
            "}";
    }
}
