package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StationDto {
    public Long id;
    @NotEmpty
    public String nameStation;
    @NotEmpty
    public Double latitude;
    @NotEmpty
    public Double longitude;
    public List<String> canGetStation = new ArrayList<>();
}
