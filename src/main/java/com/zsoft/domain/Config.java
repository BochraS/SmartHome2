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
 * A Config.
 */
@Entity
@Table(name = "config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "description")
    private String description;

    @Column(name = "des")
    private String des;

    @ManyToOne
    @JoinColumn(name = "SMART_OBJECT_ID")
    @JsonIgnoreProperties("")
    private SmartObject smartObject;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ProfileConfValue profileConfValue;

    @OneToMany(mappedBy = "config")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ConfValue> confValues = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public Config key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public Config description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDes() {
        return des;
    }

    public Config des(String des) {
        this.des = des;
        return this;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public SmartObject getSmartObject() {
        return smartObject;
    }

    public Config smartObject(SmartObject smartObject) {
        this.smartObject = smartObject;
        return this;
    }

    public void setSmartObject(SmartObject smartObject) {
        this.smartObject = smartObject;
    }

    public ProfileConfValue getProfileConfValue() {
        return profileConfValue;
    }

    public Config profileConfValue(ProfileConfValue profileConfValue) {
        this.profileConfValue = profileConfValue;
        return this;
    }

    public void setProfileConfValue(ProfileConfValue profileConfValue) {
        this.profileConfValue = profileConfValue;
    }

    public Set<ConfValue> getConfValues() {
        return confValues;
    }

    public Config confValues(Set<ConfValue> confValues) {
        this.confValues = confValues;
        return this;
    }

    public Config addConfValue(ConfValue confValue) {
        this.confValues.add(confValue);
        confValue.setConfig(this);
        return this;
    }

    public Config removeConfValue(ConfValue confValue) {
        this.confValues.remove(confValue);
        confValue.setConfig(null);
        return this;
    }

    public void setConfValues(Set<ConfValue> confValues) {
        this.confValues = confValues;
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
        Config config = (Config) o;
        if (config.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), config.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Config{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", description='" + getDescription() + "'" +
            ", des='" + getDes() + "'" +
            "}";
    }
}
