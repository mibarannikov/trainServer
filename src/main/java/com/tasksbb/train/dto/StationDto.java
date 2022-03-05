package com.tasksbb.train.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationDto {
    public Long id;
    public String nameStation;
    public Double latitude;
    public Double longitude;
    public String[] canGetStation;


}
