package com.dev.managementtaskapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateTaskDto {

    @NotEmpty
    private String title;
    private List<CreateSubTaskDto> subTasks = new ArrayList<CreateSubTaskDto>();

}
