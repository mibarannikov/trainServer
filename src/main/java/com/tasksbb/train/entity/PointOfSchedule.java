package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "point_of_schedule")
@Getter
@Setter
public class PointOfSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "station_entity_id", nullable = false)
    private StationEntity stationEntity;

    @Column(name = "arrival_time")
    private Date arrivalTime;

    @ManyToOne
    @JoinColumn(name = "train_entity_id")
    private TrainEntity trainEntity;


}
