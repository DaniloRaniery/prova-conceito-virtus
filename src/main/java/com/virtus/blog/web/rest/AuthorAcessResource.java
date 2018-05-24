package com.virtus.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.virtus.blog.service.AuthorAcessService;
import com.virtus.blog.web.rest.errors.BadRequestAlertException;
import com.virtus.blog.web.rest.util.HeaderUtil;
import com.virtus.blog.service.dto.AuthorAcessDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/author-acesses")
    @Timed
    public ResponseEntity<AuthorAcessDTO> createAuthorAcess(@Valid @RequestBody AuthorAcessDTO authorAcessDTO) throws URISyntaxException {
        log.debug("REST request to save AuthorAcess : {}", authorAcessDTO);
        if (authorAcessDTO.getId() != null) {
            throw new BadRequestAlertException("A new authorAcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorAcessDTO result = authorAcessService.save(authorAcessDTO);
        return ResponseEntity.created(new URI("/api/author-acesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /author-acesses : Updates an existing authorAcess.
     *
     * @param authorAcessDTO the authorAcessDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authorAcessDTO,
     * or with status 400 (Bad Request) if the authorAcessDTO is not valid,
     * or with status 500 (Internal Server Error) if the authorAcessDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/author-acesses")
    @Timed
    public ResponseEntity<AuthorAcessDTO> updateAuthorAcess(@Valid @RequestBody AuthorAcessDTO authorAcessDTO) throws URISyntaxException {
        log.debug("REST request to update AuthorAcess : {}", authorAcessDTO);
        if (authorAcessDTO.getId() == null) {
            return createAuthorAcess(authorAcessDTO);
        }
        AuthorAcessDTO result = authorAcessService.save(authorAcessDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, authorAcessDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /author-acesses : get all the authorAcesses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of authorAcesses in body
     */
    @GetMapping("/author-acesses")
    @Timed
    public List<AuthorAcessDTO> getAllAuthorAcesses() {
        log.debug("REST request to get all AuthorAcesses");
        return authorAcessService.findAll();
        }

    /**
     * GET  /author-acesses/:id : get the "id" authorAcess.
     *
     * @param id the id of the authorAcessDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authorAcessDTO, or with status 404 (Not Found)
     */
    @GetMapping("/author-acesses/{id}")
    @Timed
    public ResponseEntity<AuthorAcessDTO> getAuthorAcess(@PathVariable Long id) {
        log.debug("REST request to get AuthorAcess : {}", id);
        AuthorAcessDTO authorAcessDTO = authorAcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorAcessDTO));
    }

    /**
     * DELETE  /author-acesses/:id : delete the "id" authorAcess.
     *
     * @param id the id of the authorAcessDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/author-acesses/{id}")
    @Timed
    public ResponseEntity<Void> deleteAuthorAcess(@PathVariable Long id) {
        log.debug("REST request to delete AuthorAcess : {}", id);
        authorAcessService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/author-acesses?query=:query : search for the authorAcess corresponding
     * to the query.
     *
     * @param query the query of the authorAcess search
     * @return the result of the search
     */
    @GetMapping("/_search/author-acesses")
    @Timed
    public List<AuthorAcessDTO> searchAuthorAcesses(@RequestParam String query) {
        log.debug("REST request to search AuthorAcesses for query {}", query);
        return authorAcessService.search(query);
    }

}
