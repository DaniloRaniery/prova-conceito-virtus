package com.virtus.blog.service.mapper;

import com.virtus.blog.domain.*;
import com.virtus.blog.service.dto.AuthorAcessDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AuthorAcess and its DTO AuthorAcessDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorAcessMapper extends EntityMapper<AuthorAcessDTO, AuthorAcess> {



    default AuthorAcess fromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthorAcess authorAcess = new AuthorAcess();
        authorAcess.setId(id);
        return authorAcess;
    }
}
