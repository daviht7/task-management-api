package com.dev.managementtaskapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateSubTaskDto {

    @NotEmpty
    private String title;

}
