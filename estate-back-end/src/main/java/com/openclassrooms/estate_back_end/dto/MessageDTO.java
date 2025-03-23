package com.openclassrooms.estate_back_end.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class MessageDTO {

    @NotNull
    private String message;

    @NotNull
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull
    @JsonProperty("rental_id")
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
