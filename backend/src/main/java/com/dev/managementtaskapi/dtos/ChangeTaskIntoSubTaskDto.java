package com.dev.managementtaskapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

@Data
public class ChangeTaskIntoSubTaskDto {

    @NotEmpty
    private UUID taskId;

}
