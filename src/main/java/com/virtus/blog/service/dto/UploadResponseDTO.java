package com.virtus.blog.service.dto;

import java.io.Serializable;

public class UploadResponseDTO implements Serializable {

    private String assetName;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAssetName() {

        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}


