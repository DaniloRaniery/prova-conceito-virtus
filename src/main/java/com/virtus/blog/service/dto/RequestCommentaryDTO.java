package com.virtus.blog.service.dto;

import javax.validation.constraints.NotNull;

public class RequestCommentaryDTO {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
