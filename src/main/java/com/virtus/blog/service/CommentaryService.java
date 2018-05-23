package com.virtus.blog.service;

import com.virtus.blog.domain.Commentary;
import com.virtus.blog.domain.Post;
import com.virtus.blog.repository.CommentaryRepository;
import com.virtus.blog.repository.PostRepository;
import com.virtus.blog.service.dto.RequestCommentaryDTO;
import com.virtus.blog.web.rest.errors.PostNotFoundException;
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

    public CommentaryService(CommentaryRepository commentaryRepository, PostRepository postRepository) {
        this.commentaryRepository = commentaryRepository;
        this.postRepository = postRepository;
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
}
