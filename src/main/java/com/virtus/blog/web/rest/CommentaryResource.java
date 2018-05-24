package com.virtus.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.virtus.blog.domain.Commentary;

import com.virtus.blog.repository.CommentaryRepository;
import com.virtus.blog.repository.search.CommentarySearchRepository;
import com.virtus.blog.security.AuthoritiesConstants;
import com.virtus.blog.service.CommentaryService;
import com.virtus.blog.service.dto.RequestCommentaryDTO;
import com.virtus.blog.web.rest.errors.BadRequestAlertException;
import com.virtus.blog.web.rest.util.HeaderUtil;
import com.virtus.blog.web.rest.util.PaginationUtil;
import com.virtus.blog.service.dto.CommentaryDTO;
import com.virtus.blog.service.mapper.CommentaryMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Commentary.
 */
@RestController
@RequestMapping("/api")
public class CommentaryResource {

    private final Logger log = LoggerFactory.getLogger(CommentaryResource.class);

    private static final String ENTITY_NAME = "commentary";

    private final CommentaryRepository commentaryRepository;

    private final CommentaryMapper commentaryMapper;

    private final CommentaryService commentaryService;

    private final CommentarySearchRepository commentarySearchRepository;

    public CommentaryResource(CommentaryRepository commentaryRepository, CommentaryMapper commentaryMapper,
                              CommentarySearchRepository commentarySearchRepository, CommentaryService commentaryService) {
        this.commentaryRepository = commentaryRepository;
        this.commentaryMapper = commentaryMapper;
        this.commentarySearchRepository = commentarySearchRepository;
        this.commentaryService = commentaryService;
    }

    /**
     * POST  /commentaries : Create a new commentary.
     *
     * @param commentaryDTO the commentaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentaryDTO, or with status 400 (Bad Request) if the commentary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commentaries")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<CommentaryDTO> createCommentary(@Valid @RequestBody CommentaryDTO commentaryDTO) throws URISyntaxException {
        log.debug("REST request to save Commentary : {}", commentaryDTO);
        if (commentaryDTO.getUserId() == null || commentaryDTO.getPostId() == null) {
            throw new BadRequestAlertException("A new commentary needs a user and post id", ENTITY_NAME, "idnotexists");
        }

        CommentaryDTO result = commentaryService.createAndUpdateCommentary(commentaryDTO);

        return ResponseEntity.created(new URI("/api/commentaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commentaries : Updates an existing commentary.
     *
     * @param commentaryDTO the commentaryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentaryDTO,
     * or with status 400 (Bad Request) if the commentaryDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentaryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commentaries")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<CommentaryDTO> updateCommentary(@Valid @RequestBody CommentaryDTO commentaryDTO) throws URISyntaxException {
        log.debug("REST request to update Commentary : {}", commentaryDTO);

        if (commentaryDTO.getUserId() == null || commentaryDTO.getPostId() == null) {
            throw new BadRequestAlertException("A new commentary needs a user and post id", ENTITY_NAME, "idnotexists");
        }

        CommentaryDTO result = commentaryService.createAndUpdateCommentary(commentaryDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commentaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commentaries : get all the commentaries for post.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentaries in body
     */
    @GetMapping("/commentaries")
    @Timed
    public ResponseEntity<List<CommentaryDTO>> getAllCommentariesForPost(RequestCommentaryDTO requestCommentaryDTO, Pageable pageable) {
        log.debug("REST request to get a page of Commentaries");
        Page<Commentary> page = commentaryService.getAllCommentsForUser(requestCommentaryDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentaries");
        return new ResponseEntity<>(commentaryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentaries/:id : get the "id" commentary.
     *
     * @param id the id of the commentaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commentaries/{id}")
    @Timed
    public ResponseEntity<CommentaryDTO> getCommentary(@PathVariable Long id) {
        log.debug("REST request to get Commentary : {}", id);
        Commentary commentary = commentaryRepository.findOne(id);
        CommentaryDTO commentaryDTO = commentaryMapper.toDto(commentary);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commentaryDTO));
    }

    /**
     * DELETE  /commentaries/:id : delete the "id" commentary.
     *
     * @param id the id of the commentaryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commentaries/{id}")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Void> deleteCommentary(@PathVariable Long id) {
        log.debug("REST request to delete Commentary : {}", id);
        commentaryRepository.delete(id);
        commentarySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/commentaries?query=:query : search for the commentary corresponding
     * to the query.
     *
     * @param query    the query of the commentary search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/commentaries")
    @Timed
    public ResponseEntity<List<CommentaryDTO>> searchCommentaries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Commentaries for query {}", query);
        Page<Commentary> page = commentarySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commentaries");
        return new ResponseEntity<>(commentaryMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
