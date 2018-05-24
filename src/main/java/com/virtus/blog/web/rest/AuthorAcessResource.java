package com.virtus.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.virtus.blog.security.AuthoritiesConstants;
import com.virtus.blog.service.AuthorAcessService;
import com.virtus.blog.web.rest.errors.BadRequestAlertException;
import com.virtus.blog.web.rest.util.HeaderUtil;
import com.virtus.blog.service.dto.AuthorAcessDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AuthorAcess.
 */
@RestController
@RequestMapping("/api")
public class AuthorAcessResource {

    private final Logger log = LoggerFactory.getLogger(AuthorAcessResource.class);

    private static final String ENTITY_NAME = "authorAcess";

    private final AuthorAcessService authorAcessService;

    public AuthorAcessResource(AuthorAcessService authorAcessService) {
        this.authorAcessService = authorAcessService;
    }

    /**
     * POST  /author-acesses : Create a new authorAcess.
     *
     * @param authorAcessDTO the authorAcessDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authorAcessDTO, or with status 400 (Bad Request) if the authorAcess has already an ID
     */
    @PostMapping("/author-acesses")
    @Timed
    public ResponseEntity<AuthorAcessDTO> createAuthorAcess(@Valid @RequestBody AuthorAcessDTO authorAcessDTO) throws URISyntaxException {
        log.debug("REST request to save AuthorAcess : {}", authorAcessDTO);
        AuthorAcessDTO result = authorAcessService.save(authorAcessDTO);
        return ResponseEntity.created(new URI("/api/author-acesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /author-acesses : get all the authorAcesses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of authorAcesses in body
     */
    @GetMapping("/author-acesses")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<AuthorAcessDTO> getAllAuthorAcesses() {
        log.debug("REST request to get all AuthorAcesses");
        return authorAcessService.findAll();
        }

    /**
     * DELETE  /author-acesses/:id : delete the "id" authorAcess.
     *
     * @param id the id of the authorAcessDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/author-acesses/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> denyAuthorAcess(@PathVariable Long id) {
        log.debug("REST request to delete AuthorAcess : {}", id);
        authorAcessService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /author-acesses/:id : delete the "id" authorAcess.
     *
     * @param id the id of the authorAcessDTO to allow access
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/author-allow-acesses/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> allowAuthorAcess(@PathVariable Long id) {
        log.debug("REST request to delete AuthorAcess : {}", id);
        authorAcessService.allowAccess(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
