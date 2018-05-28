package com.virtus.blog.web.rest;

import com.virtus.blog.service.FileService;
import com.virtus.blog.service.dto.UploadResponseDTO;
import com.virtus.blog.web.rest.util.HeaderUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file")
public class UploadResource {

    private static final String ENTITY_NAME = "uploadfile";

    private final FileService fileService;

    public UploadResource(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public ResponseEntity<UploadResponseDTO> singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws URISyntaxException {

        UploadResponseDTO uploadResponseDTO = fileService.uploadFiles(file);

        return ResponseEntity.created(new URI("/api/upload/" + file.getOriginalFilename()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, file.getName())).body(uploadResponseDTO);
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> singleFileDownloading(@PathVariable String filename) {

        Resource file = fileService.downloadingFile(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
