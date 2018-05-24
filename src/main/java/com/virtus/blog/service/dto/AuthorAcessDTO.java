package com.virtus.blog.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AuthorAcess entity.
 */
public class AuthorAcessDTO implements Serializable {

    private Long id;

    @NotNull
    private String requestLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestLogin() {
        return requestLogin;
    }

    public void setRequestLogin(String requestLogin) {
        this.requestLogin = requestLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorAcessDTO authorAcessDTO = (AuthorAcessDTO) o;
        if(authorAcessDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorAcessDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthorAcessDTO{" +
            "id=" + getId() +
            ", requestLogin='" + getRequestLogin() + "'" +
            "}";
    }
}
