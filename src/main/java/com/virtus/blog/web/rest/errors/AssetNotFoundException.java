package com.virtus.blog.web.rest.errors;

public class AssetNotFoundException extends BadRequestAlertException {

    public AssetNotFoundException() {
        super(ErrorConstants.ASSET_NOT_FOUND_TYPE, "Asset id not found in database.", "id", "assetnotfound");
    }
}
