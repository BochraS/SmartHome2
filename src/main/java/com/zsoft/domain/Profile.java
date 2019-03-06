package com.zsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "age")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ProfileConfValue profileConfValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public Profile lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Profile firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getAge() {
        return age;
    }

    public Profile age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProfileConfValue getProfileConfValue() {
        return profileConfValue;
    }

    public Profile profileConfValue(ProfileConfValue profileConfValue) {
        this.profileConfValue = profileConfValue;
        return this;
    }

    public void setProfileConfValue(ProfileConfValue profileConfValue) {
        this.profileConfValue = profileConfValue;
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
        Profile profile = (Profile) o;
        if (profile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
