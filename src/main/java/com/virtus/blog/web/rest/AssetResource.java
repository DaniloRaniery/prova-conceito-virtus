package com.virtus.blog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.virtus.blog.domain.Asset;

import com.virtus.blog.repository.AssetRepository;
import com.virtus.blog.repository.search.AssetSearchRepository;
import com.virtus.blog.service.AssetService;
import com.virtus.blog.web.rest.errors.BadRequestAlertException;
import com.virtus.blog.web.rest.util.HeaderUtil;
import com.virtus.blog.service.dto.AssetDTO;
import com.virtus.blog.service.mapper.AssetMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Asset.
 */
@RestController
@RequestMapping("/api")
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    private static final String ENTITY_NAME = "asset";

    private final AssetRepository assetRepository;

    private final AssetMapper assetMapper;

    private final AssetService assetService;

    private final AssetSearchRepository assetSearchRepository;

    public AssetResource(AssetRepository assetRepository, AssetMapper assetMapper, AssetSearchRepository assetSearchRepository,
                         AssetService assetService) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
        this.assetSearchRepository = assetSearchRepository;
        this.assetService = assetService;
    }

    /**
     * POST  /assets : Create a new asset.
     *
     * @param file the file to create
     * @return the ResponseEntity with status 201 (Created) and with id of the new file
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assets")
    @Timed
    public ResponseEntity<Long> createAsset(@RequestBody @RequestParam("file") MultipartFile file) throws URISyntaxException {


        Long newAssetId = assetService.uploadAsset(file);

        return ResponseEntity.created(new URI("/api/assets/" + newAssetId))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, newAssetId.toString()))
            .body(newAssetId);
    }

    @GetMapping("/{assetId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> singleAssetDownloading(@PathVariable Long assetId) {

        Resource file = assetService.downloadingAsset(assetId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    /**
     * DELETE  /assets/:id : delete the "id" asset.
     *
     * @param id the id of the assetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assets/{id}")
    @Timed
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) throws IOException {
        log.debug("REST request to delete Asset : {}", id);

        assetService.deleteAsset(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
