package com.zsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SmartObject.
 */
@Entity
@Table(name = "smart_object")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "smartobject") //for mongodb or elastic search
public class SmartObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "jhi_shared")
    private Boolean shared;

    @Column(name = "fabriquant")
    private String fabriquant;

    @Column(name = "year_of_fabriquation")
    private String yearOfFabriquation;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnoreProperties("") //use to exclude class fields from serialization and deserialization process.
    private User user;

    @OneToMany(mappedBy = "smartObject")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Config> configs = new HashSet<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public SmartObject type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isShared() {
        return shared;
    }

    public SmartObject shared(Boolean shared) {
        this.shared = shared;
        return this;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String getFabriquant() {
        return fabriquant;
    }

    public SmartObject fabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
        return this;
    }

    public void setFabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
    }

    public String getYearOfFabriquation() {
        return yearOfFabriquation;
    }

    public SmartObject yearOfFabriquation(String yearOfFabriquation) {
        this.yearOfFabriquation = yearOfFabriquation;
        return this;
    }

    public void setYearOfFabriquation(String yearOfFabriquation) {
        this.yearOfFabriquation = yearOfFabriquation;
    }

    public User getUser() {
        return user;
    }

    public SmartObject user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Config> getConfigs() {
        return configs;
    }

    public SmartObject configs(Set<Config> configs) {
        this.configs = configs;
        return this;
    }

    public SmartObject addConfig(Config config) {
        this.configs.add(config);
        config.setSmartObject(this);
        return this;
    }

    public SmartObject removeConfig(Config config) {
        this.configs.remove(config);
        config.setSmartObject(null);
        return this;
    }

    public void setConfigs(Set<Config> configs) {
        this.configs = configs;
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
        SmartObject smartObject = (SmartObject) o;
        if (smartObject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smartObject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmartObject{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", shared='" + isShared() + "'" +
            ", fabriquant='" + getFabriquant() + "'" +
            ", yearOfFabriquation='" + getYearOfFabriquation() + "'" +
            "}";
    }
}
