package com.virtus.blog.repository;

import com.virtus.blog.domain.AuthorAcess;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AuthorAcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorAcessRepository extends JpaRepository<AuthorAcess, Long> {

}
