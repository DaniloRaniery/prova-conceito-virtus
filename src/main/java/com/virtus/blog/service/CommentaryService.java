package com.virtus.blog.service;

import com.virtus.blog.domain.Commentary;
import com.virtus.blog.domain.Post;
import com.virtus.blog.repository.CommentaryRepository;
import com.virtus.blog.repository.PostRepository;
import com.virtus.blog.repository.UserRepository;
import com.virtus.blog.repository.search.CommentarySearchRepository;
import com.virtus.blog.service.dto.CommentaryDTO;
import com.virtus.blog.service.dto.RequestCommentaryDTO;
import com.virtus.blog.service.mapper.CommentaryMapper;
import com.virtus.blog.web.rest.errors.PostNotFoundException;
import com.virtus.blog.web.rest.errors.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Commentary.
 */
@Service
@Transactional
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentaryMapper commentaryMapper;

    private final CommentarySearchRepository commentarySearchRepository;

    public CommentaryService(CommentaryRepository commentaryRepository, PostRepository postRepository,
                             CommentaryMapper commentaryMapper, CommentarySearchRepository commentarySearchRepository,
                             UserRepository userRepository) {
        this.commentaryRepository = commentaryRepository;
        this.postRepository = postRepository;
        this.commentaryMapper = commentaryMapper;
        this.commentarySearchRepository = commentarySearchRepository;
        this.userRepository = userRepository;

    }

    public Page<Commentary> getAllCommentsForUser(RequestCommentaryDTO requestCommentaryDTO, Pageable pageable) {

        Post post = postRepository.findOne(requestCommentaryDTO.getId());

        if (post == null) {
            throw new PostNotFoundException();
        }

        List<Commentary> list = new ArrayList<>();
        for (Commentary commentary : post.getCommentaries()) {
            list.add(commentary);
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    public CommentaryDTO createAndUpdateCommentary(CommentaryDTO commentaryDTO) {

        if (postRepository.findOne(commentaryDTO.getPostId()) == null) {
            throw new PostNotFoundException();
        }

        if (userRepository.findOne(commentaryDTO.getUserId()) == null) {
            throw new UserNotFoundException();
        }

        Commentary commentary = commentaryMapper.toEntity(commentaryDTO);
        commentary = commentaryRepository.save(commentary);
        CommentaryDTO result = commentaryMapper.toDto(commentary);
        commentarySearchRepository.save(commentary);



        return result;
    }
}

