package com.virtus.blog.service;

import com.virtus.blog.domain.AuthorAcess;
import com.virtus.blog.domain.Authority;
import com.virtus.blog.domain.User;
import com.virtus.blog.repository.AuthorAcessRepository;
import com.virtus.blog.repository.UserRepository;
import com.virtus.blog.repository.search.AuthorAcessSearchRepository;
import com.virtus.blog.service.dto.AuthorAcessDTO;
import com.virtus.blog.service.dto.UserDTO;
import com.virtus.blog.service.mapper.AuthorAcessMapper;
import com.virtus.blog.service.mapper.UserMapper;
import com.virtus.blog.web.rest.errors.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserService userService;

    private final AuthorAcessMapper authorAcessMapper;

    private final AuthorAcessSearchRepository authorAcessSearchRepository;


    public AuthorAcessService(AuthorAcessRepository authorAcessRepository, AuthorAcessMapper authorAcessMapper,
                              AuthorAcessSearchRepository authorAcessSearchRepository, UserRepository userRepository,
                              UserMapper userMapper, UserService userService) {
        this.authorAcessRepository = authorAcessRepository;
        this.authorAcessMapper = authorAcessMapper;
        this.authorAcessSearchRepository = authorAcessSearchRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
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

    /**
     * Allow access to the sent user.
     *
     * @param id the id of the entity
     */
    public void allowAccess(Long id) {

        AuthorAcess author = authorAcessRepository.findOne(id);

        Authority authorityAuthor = new Authority();
        authorityAuthor.setName("ROLE_AUTHOR");
        Authority authorityUser = new Authority();
        authorityUser.setName("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityAuthor);
        authorities.add(authorityUser);

        if (author == null) {
            throw new UserNotFoundException();
        }

        Optional<User> user = userRepository.findOneByLogin(author.getRequestLogin());

        if (user.isPresent()){
            User userDomain = user.get();
            userDomain.setAuthorities(authorities);
            UserDTO userDTO = userMapper.userToUserDTO(userDomain);
            userService.updateUser(userDTO);

        }else{
            throw new UserNotFoundException();
        }

        this.delete(id);
    }
}
