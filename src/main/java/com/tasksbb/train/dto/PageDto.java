package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageDto {
    private List<TrainDto> content = new ArrayList<>();
    private Long totalElements;
}
