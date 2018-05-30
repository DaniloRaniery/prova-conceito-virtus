package com.virtus.blog.service;

import com.virtus.blog.domain.Asset;
import com.virtus.blog.repository.AssetRepository;
import com.virtus.blog.repository.search.AssetSearchRepository;
import com.virtus.blog.service.mapper.AssetMapper;
import com.virtus.blog.web.rest.errors.AssetNotFoundException;
import org.apache.commons.io.FilenameUtils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AssetService {

    private static String UPLOADED_FOLDER = "src/main/images/";

    private final Path rootLocation = Paths.get(UPLOADED_FOLDER);

    private final AssetRepository assetRepository;

    private final AssetMapper assetMapper;

    private final AssetSearchRepository assetSearchRepository;


    public AssetService(AssetRepository assetRepository, AssetMapper assetMapper, AssetSearchRepository assetSearchRepository) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
        this.assetSearchRepository = assetSearchRepository;
    }

    public Long uploadAsset(MultipartFile file) {

        Asset asset = new Asset();

        try {
            Files.createDirectories(Paths.get(UPLOADED_FOLDER));
            MultipartFile multipartFile = new MockMultipartFile(FilenameUtils.getBaseName(file.
                getOriginalFilename()).concat(new SimpleDateFormat("yyyyMMddHHmm").format(new Date())) + "." +
                FilenameUtils.getExtension(file.getOriginalFilename()), file.getInputStream());

            Files.copy(file.getInputStream(), this.rootLocation.resolve(multipartFile.getName()));

            Path path = rootLocation.resolve(multipartFile.getName());
            Resource resource = new UrlResource(path.toUri());
            asset.setAssetPath(resource.getURL().toString());
            asset.setAssetName(multipartFile.getName());
            asset.setAssetType(FilenameUtils.getExtension(file.getOriginalFilename()));
            asset = assetRepository.save(asset);
            assetSearchRepository.save(asset);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed!");
        }

        return asset.getId();
    }

    public Resource downloadingAsset(Long assetId) {
        try {
            Asset asset = assetRepository.findOne(assetId);
            Path file = rootLocation.resolve(asset.getAssetName());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL not found!");
        }
    }

    public void deleteAsset(Long id) throws IOException {

        Asset asset = assetRepository.findOne(id);

        if(asset == null){
            throw new AssetNotFoundException();
        }
        Path path = rootLocation.resolve(asset.getAssetName());

        assetRepository.delete(id);
        assetSearchRepository.delete(id);

        Files.delete(path);
    }
}
