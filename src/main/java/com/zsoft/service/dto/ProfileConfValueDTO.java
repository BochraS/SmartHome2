package com.zsoft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ProfileConfValue entity.
 */
public class ProfileConfValueDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfileConfValueDTO profileConfValueDTO = (ProfileConfValueDTO) o;
        if (profileConfValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileConfValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileConfValueDTO{" +
            "id=" + getId() +
            "}";
    }
}
