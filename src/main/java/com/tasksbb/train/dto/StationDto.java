package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StationDto {
    public Long id;
    public String nameStation;
    public Double latitude;
    public Double longitude;
    public List<String> canGetStation = new ArrayList<>();
}
