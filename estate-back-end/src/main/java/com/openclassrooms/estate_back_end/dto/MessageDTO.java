package com.openclassrooms.estate_back_end.dto;

import jakarta.validation.constraints.NotNull;

public class MessageDTO {

    @NotNull
    private Integer userId;

    @NotNull
    private String message;

    @NotNull
    private Integer rentalId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }
}
