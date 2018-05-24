package com.virtus.blog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AuthorAcess.
 */
@Entity
@Table(name = "author_acess")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "authoracess")
public class AuthorAcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "request_login", nullable = false)
    private String requestLogin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestLogin() {
        return requestLogin;
    }

    public AuthorAcess requestLogin(String requestLogin) {
        this.requestLogin = requestLogin;
        return this;
    }

    public void setRequestLogin(String requestLogin) {
        this.requestLogin = requestLogin;
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
        AuthorAcess authorAcess = (AuthorAcess) o;
        if (authorAcess.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorAcess.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthorAcess{" +
            "id=" + getId() +
            ", requestLogin='" + getRequestLogin() + "'" +
            "}";
    }
}
