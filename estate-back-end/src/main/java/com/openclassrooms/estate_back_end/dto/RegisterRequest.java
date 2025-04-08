package com.openclassrooms.estate_back_end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {

    @NotEmpty
    @Email
    @Pattern(regexp = "^[A-Za-z0-9&~'{|`_^}=+$%*?/!]+@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)?$")
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
