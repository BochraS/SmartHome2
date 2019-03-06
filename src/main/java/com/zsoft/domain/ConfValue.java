package com.zsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ConfValue.
 */
@Entity
@Table(name = "conf_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "confvalue")
public class ConfValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Config config;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public ConfValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Config getConfig() {
        return config;
    }

    public ConfValue config(Config config) {
        this.config = config;
        return this;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfValue confValue = (ConfValue) o;
        if (confValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
