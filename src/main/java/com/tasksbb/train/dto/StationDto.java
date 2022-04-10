package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StationDto {
    private Long id;
    @NotEmpty
    private String nameStation;
    @NotEmpty
    private Double latitude;
    @NotEmpty
    private Double longitude;
    private List<String> canGetStation = new ArrayList<>();
}
