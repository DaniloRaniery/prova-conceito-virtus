package com.virtus.blog.service.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import java.util.List;

/**
 * A DTO for to create Post entity.
 */
public class RequestPostDTO {

    @NotBlank
    private String title;

    @NotNull
    private ZonedDateTime date;

    @NotBlank
    private String bodyText;

    @NotNull
    private Long authorId;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    private List<Long> assets = new ArrayList<Long>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public List<Long> getAssets() {
        return assets;
    }

    public void setAssets(List<Long> assets) {
        this.assets = assets;
    }
}
