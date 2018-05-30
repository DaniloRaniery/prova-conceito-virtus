package com.virtus.blog.service;

import com.virtus.blog.domain.Asset;
import com.virtus.blog.domain.Body;
import com.virtus.blog.repository.AssetRepository;
import com.virtus.blog.repository.BodyRepository;
import com.virtus.blog.repository.search.AssetSearchRepository;
import com.virtus.blog.repository.search.BodySearchRepository;
import com.virtus.blog.service.dto.AssetDTO;
import com.virtus.blog.service.dto.PostDTO;
import com.virtus.blog.service.dto.RequestPostDTO;
import com.virtus.blog.service.mapper.PostMapper;
import com.virtus.blog.web.rest.errors.PostNotFoundException;
import org.apache.commons.collections.ArrayStack;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Body and Assets.
 */
@Service
@Transactional
public class BodyService {

    private final BodyRepository bodyRepository;

    private final AssetRepository assetRepository;

    private final PostMapper postMapper;

    private final BodySearchRepository bodySearchRepository;

    private final AssetSearchRepository assetSearchRepository;

    public BodyService(BodyRepository bodyRepository, AssetRepository assetRepository, PostMapper postMapper,
                       BodySearchRepository bodySearchRepository, AssetSearchRepository assetSearchRepository) {
        this.bodyRepository = bodyRepository;
        this.assetRepository = assetRepository;
        this.postMapper = postMapper;
        this.bodySearchRepository = bodySearchRepository;
        this.assetSearchRepository = assetSearchRepository;
    }

    /**
     * Update all information for a specific body, and return the modified body.
     *
     * @param bodyId body to update
     * @throws PostNotFoundException 400 (Bad Request) if the body id is not found
     */
    public void updateBody(Long bodyId, String textBody) {

        Body body = bodyRepository
            .findOne(bodyId);

        if (body == null) {
            throw new PostNotFoundException();
        }

        body.setText(textBody);

        body = bodyRepository.save(body);
        bodySearchRepository.save(body);
    }

    /**
     * Create a body.
     *
     * @param requestPostDTO the request data to create Post
     * @param postDTO        the post being generated
     * @return the created body
     */
    public Body createBody(RequestPostDTO requestPostDTO, PostDTO postDTO) {

        Body body = new Body();
        body.setText(requestPostDTO.getBodyText());
        body.setPost(postMapper.toEntity(postDTO));
        bodyRepository.save(body);
        body.setAssets(this.createAsset(requestPostDTO.getAssets(), body));

        return body;
    }

    /**
     * Create a assets list.
     *
     * @param assetsToCreate the request data to create assets
     * @param body           the body being generated
     * @return the assets list
     */
    private Set<Asset> createAsset(List<Long> assetsToCreate, Body body) {

        Set<Asset> assets = new HashSet<>();
        for (Long asset : assetsToCreate) {
            Asset newAsset = assetRepository.findOne(asset);
            if (newAsset != null) {
                newAsset.setBody(body);
                Asset savedAsset = assetRepository.save(newAsset);
                assets.add(savedAsset);
            }
        }

        return assets;
    }

    /**
     * Get all assets from sent body
     *
     * @param bodyId the body id with assets
     * @return the assets list
     */
    public List<String> getAssets(Long bodyId) {

        Body body = bodyRepository.findOne(bodyId);
        List<String> listToReturn = new ArrayList<>();

        for (Asset asset : body.getAssets()) {
            listToReturn.add(asset.getAssetName());
        }
        return listToReturn;
    }
}
