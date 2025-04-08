package com.openclassrooms.estate_back_end.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "picture")
public class PictureStorageProperties {

    private String uploadDir;

    private String serverUrl;

    private String pictureUrlPath;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getPictureUrlPath() {
        return pictureUrlPath;
    }

    public void setPictureUrlPath(String pictureUrlPath) {
        this.pictureUrlPath = pictureUrlPath;
    }

}
