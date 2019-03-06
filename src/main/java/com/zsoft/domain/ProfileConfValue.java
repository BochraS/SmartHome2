package com.zsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProfileConfValue.
 */
@Entity
@Table(name = "profile_conf_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "profileconfvalue")
public class ProfileConfValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "profileConfValue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Config> configs = new HashSet<>();
    @OneToMany(mappedBy = "profileConfValue")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profile> profiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Config> getConfigs() {
        return configs;
    }

    public ProfileConfValue configs(Set<Config> configs) {
        this.configs = configs;
        return this;
    }

    public ProfileConfValue addConfig(Config config) {
        this.configs.add(config);
        config.setProfileConfValue(this);
        return this;
    }

    public ProfileConfValue removeConfig(Config config) {
        this.configs.remove(config);
        config.setProfileConfValue(null);
        return this;
    }

    public void setConfigs(Set<Config> configs) {
        this.configs = configs;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public ProfileConfValue profiles(Set<Profile> profiles) {
        this.profiles = profiles;
        return this;
    }

    public ProfileConfValue addProfile(Profile profile) {
        this.profiles.add(profile);
        profile.setProfileConfValue(this);
        return this;
    }

    public ProfileConfValue removeProfile(Profile profile) {
        this.profiles.remove(profile);
        profile.setProfileConfValue(null);
        return this;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
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
        ProfileConfValue profileConfValue = (ProfileConfValue) o;
        if (profileConfValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileConfValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileConfValue{" +
            "id=" + getId() +
            "}";
    }
}
