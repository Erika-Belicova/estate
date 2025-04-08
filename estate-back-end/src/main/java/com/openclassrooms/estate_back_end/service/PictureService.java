package com.openclassrooms.estate_back_end.service;

import com.openclassrooms.estate_back_end.configuration.PictureStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PictureService {

    private final PictureStorageProperties pictureStorageProperties;

    @Autowired
    public PictureService(PictureStorageProperties pictureStorageProperties) {
        this.pictureStorageProperties = pictureStorageProperties;
    }

    public String uploadPicture(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg"))) {
            throw new IllegalArgumentException("Only JPEG images are allowed");
        }

        Path uploadPath = Paths.get(pictureStorageProperties.getUploadDir());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }

        return filePath.toString();
    }

    public String getPicture(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString();
        return pictureStorageProperties.getServerUrl() + pictureStorageProperties.getPictureUrlPath() + "/" + fileName;
    }

}