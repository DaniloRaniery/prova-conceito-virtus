package com.virtus.blog.service;

import com.virtus.blog.domain.AuthorAcess;
import com.virtus.blog.repository.AuthorAcessRepository;
import com.virtus.blog.repository.search.AuthorAcessSearchRepository;
import com.virtus.blog.service.dto.AuthorAcessDTO;
import com.virtus.blog.service.mapper.AuthorAcessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AuthorAcess.
 */
@Service
@Transactional
public class AuthorAcessService {

    private final Logger log = LoggerFactory.getLogger(AuthorAcessService.class);

    private final AuthorAcessRepository authorAcessRepository;

    private final AuthorAcessMapper authorAcessMapper;

    private final AuthorAcessSearchRepository authorAcessSearchRepository;

    public AuthorAcessService(AuthorAcessRepository authorAcessRepository, AuthorAcessMapper authorAcessMapper, AuthorAcessSearchRepository authorAcessSearchRepository) {
        this.authorAcessRepository = authorAcessRepository;
        this.authorAcessMapper = authorAcessMapper;
        this.authorAcessSearchRepository = authorAcessSearchRepository;
    }

    /**
     * Save a authorAcess.
     *
     * @param authorAcessDTO the entity to save
     * @return the persisted entity
     */
    public AuthorAcessDTO save(AuthorAcessDTO authorAcessDTO) {
        log.debug("Request to save AuthorAcess : {}", authorAcessDTO);
        AuthorAcess authorAcess = authorAcessMapper.toEntity(authorAcessDTO);
        authorAcess = authorAcessRepository.save(authorAcess);
        AuthorAcessDTO result = authorAcessMapper.toDto(authorAcess);
        authorAcessSearchRepository.save(authorAcess);
        return result;
    }

    /**
     * Get all the authorAcesses.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AuthorAcessDTO> findAll() {
        log.debug("Request to get all AuthorAcesses");
        return authorAcessRepository.findAll().stream()
            .map(authorAcessMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one authorAcess by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AuthorAcessDTO findOne(Long id) {
        log.debug("Request to get AuthorAcess : {}", id);
        AuthorAcess authorAcess = authorAcessRepository.findOne(id);
        return authorAcessMapper.toDto(authorAcess);
    }

    /**
     * Delete the authorAcess by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AuthorAcess : {}", id);
        authorAcessRepository.delete(id);
        authorAcessSearchRepository.delete(id);
    }

    /**
     * Search for the authorAcess corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AuthorAcessDTO> search(String query) {
        log.debug("Request to search AuthorAcesses for query {}", query);
        return StreamSupport
            .stream(authorAcessSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(authorAcessMapper::toDto)
            .collect(Collectors.toList());
    }
}
