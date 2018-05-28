package com.virtus.blog.service;

import com.virtus.blog.service.dto.UploadResponseDTO;
import org.apache.commons.io.FilenameUtils;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService {

    private static String UPLOADED_FOLDER = "src/main/images/";
    private final Path rootLocation = Paths.get(UPLOADED_FOLDER);

    public UploadResponseDTO uploadFiles(MultipartFile file) {

        UploadResponseDTO uploadResponseDTO = new UploadResponseDTO();

        try {
            Files.createDirectories(Paths.get(UPLOADED_FOLDER));
            MultipartFile multipartFile = new MockMultipartFile(FilenameUtils.getBaseName(file.
                getOriginalFilename()).concat(new SimpleDateFormat("yyyyMMddHHmm").format(new Date())) + "." +
                FilenameUtils.getExtension(file.getOriginalFilename()), file.getInputStream());

            Files.copy(file.getInputStream(), this.rootLocation.resolve(multipartFile.getName()));

            uploadResponseDTO.setMessage("You successfully uploaded - " + multipartFile.getName());
            uploadResponseDTO.setAssetName(multipartFile.getName());
        } catch (Exception e) {
            throw new RuntimeException("Upload failed!");
        }
        return uploadResponseDTO;
    }

    public Resource downloadingFile(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }
}
