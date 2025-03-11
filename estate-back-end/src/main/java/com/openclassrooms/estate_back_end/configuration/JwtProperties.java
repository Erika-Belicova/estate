package com.openclassrooms.estate_back_end.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")

public class JwtProperties {

    private String secretKey;
    private String usernameUser;
    private String passwordUser;
    private String rolesUser;

    public String getRolesUser() {
        return rolesUser;
    }

    public void setRolesUser(String rolesUser) {
        this.rolesUser = rolesUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
