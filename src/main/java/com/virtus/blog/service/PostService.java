package com.virtus.blog.service;

import com.virtus.blog.domain.Asset;
import com.virtus.blog.domain.Body;
import com.virtus.blog.domain.Post;
import com.virtus.blog.repository.AssetRepository;
import com.virtus.blog.repository.BodyRepository;
import com.virtus.blog.repository.PostRepository;
import com.virtus.blog.repository.UserRepository;
import com.virtus.blog.repository.search.PostSearchRepository;
import com.virtus.blog.service.dto.BodyDTO;
import com.virtus.blog.service.dto.PostDTO;
import com.virtus.blog.service.dto.RequestPostDTO;
import com.virtus.blog.service.dto.UpdatePostDTO;
import com.virtus.blog.service.mapper.BodyMapper;
import com.virtus.blog.service.mapper.PostMapper;
import com.virtus.blog.web.rest.errors.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Post.
 */
@Service
@Transactional
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    private final BodyRepository bodyRepository;

    private final UserRepository userRepository;

    private final BodyService bodyService;

    private final PostMapper postMapper;

    private final BodyMapper bodyMapper;

    private final PostSearchRepository postSearchRepository;

    public PostService(PostRepository postRepository, PostMapper postMapper, PostSearchRepository postSearchRepository,
                       BodyService bodyService, BodyRepository bodyRepository, BodyMapper bodyMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postSearchRepository = postSearchRepository;
        this.bodyService = bodyService;
        this.bodyRepository = bodyRepository;
        this.bodyMapper = bodyMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a post.
     *
     * @param postDTO the entity to save
     * @return the persisted entity
     */
    public PostDTO save(PostDTO postDTO) {
        log.debug("Request to save Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post.setAuthor(userRepository.findOne(postDTO.getAuthorId()));
        post = postRepository.save(post);
        PostDTO result = postMapper.toDto(post);
        postSearchRepository.save(post);
        return result;
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable)
            .map(postMapper::toDto);
    }

    /**
     * Get one post by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PostDTO findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        Post post = postRepository.findOne(id);
        return postMapper.toDto(post);
    }

    /**
     * Delete the post by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.delete(id);
        postSearchRepository.delete(id);
    }

    /**
     * Search for the post corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PostDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Posts for query {}", query);
        Page<Post> result = postSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(postMapper::toDto);
    }

    /**
     * Update all information for a specific post, and return the modified post.
     *
     * @param updatePostDTO post to update
     * @return updated post
     * @throws PostNotFoundException 400 (Bad Request) if the post id is not found
     */
    public PostDTO updatePost(UpdatePostDTO updatePostDTO) {

        PostDTO postToUpdate = this.findOne((updatePostDTO.getId()));
        Body body = bodyRepository.findOne(postToUpdate.getBodyId());
        postToUpdate.setTextBody(body.getText());
        postToUpdate.setAssets(bodyService.getAssets(body.getId()));
        postToUpdate.setAuthorId(updatePostDTO.getAuthorId());

        if (postToUpdate == null) {
            throw new PostNotFoundException();
        }

        if (updatePostDTO.getTitle() != null) {
            postToUpdate.setTitle(updatePostDTO.getTitle());
        }
        if (updatePostDTO.getTextBody() != null) {
            postToUpdate.setTextBody(updatePostDTO.getTextBody());
        }
        if (updatePostDTO.getDate() != null) {
            postToUpdate.setDate(updatePostDTO.getDate());
        }

        return this.updatePost(postToUpdate);
    }

    /**
     * Update all information for a specific post, and return the modified post.
     *
     * @param postDTO post to update
     * @return updated post
     * @throws PostNotFoundException 400 (Bad Request) if the post id is not found
     */
    public PostDTO updatePost(PostDTO postDTO) {

        PostDTO postDTOToReturn = this.save(postDTO);
        bodyService.updateBody(postDTO.getBodyId(), postDTO.getTextBody());

        BodyDTO bodyDTO = bodyMapper.toDto(bodyRepository.findOne(postDTO.getBodyId()));

        postDTOToReturn.setTextBody(bodyDTO.getText());
        postDTOToReturn.setAssets(bodyService.getAssets(bodyDTO.getId()));
        postDTOToReturn.setAuthorId(postDTO.getAuthorId());

        return postDTOToReturn;
    }

    /**
     * Create a post.
     *
     * @param requestPostDTO the entity to create
     * @return the persisted entity
     */
    public PostDTO createPost(RequestPostDTO requestPostDTO) {

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(requestPostDTO.getTitle());
        postDTO.setDate(requestPostDTO.getDate());
        postDTO.setAuthorId(requestPostDTO.getAuthorId());
        postDTO = this.save(postDTO);
        Body body = (bodyService.createBody(requestPostDTO, postDTO));
        postDTO.setBodyId(body.getId());
        postDTO.setTextBody(body.getText());
        postDTO.setAssets(bodyService.getAssets(body.getId()));
        postDTO.setAuthorId(requestPostDTO.getAuthorId());
        this.updatePost(postDTO);
        return postDTO;
    }

    /**
     * Get posts from sent page
     *
     * @param page the postDTO page
     * @return the postDTO list
     */
    public List<PostDTO> getPostDTOFormat(Page<PostDTO> page) {

        postSearchRepository.deleteAll();
        List<PostDTO> pagesToReturn = new ArrayList<>();
        for (PostDTO post : page.getContent()) {
            PostDTO postDTO = new PostDTO();
            Body body = bodyRepository.findOne(post.getBodyId());
            postDTO.setTextBody(body.getText());
            postDTO.setAssets(bodyService.getAssets(body.getId()));
            postDTO.setTitle(post.getTitle());
            postDTO.setDate(post.getDate());
            postDTO.setBodyId(post.getBodyId());
            postDTO.setId(post.getId());
            pagesToReturn.add(postDTO);
        }
        return pagesToReturn;
    }
}
