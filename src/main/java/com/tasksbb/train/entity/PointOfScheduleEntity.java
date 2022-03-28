package com.tasksbb.train.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tasksbb.train.entity.enums.EStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "point_of_schedule_entity")
@Getter
@Setter
public class PointOfScheduleEntity  extends DateCreateUpdate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "station_entity_id", nullable = false)
    private StationEntity stationEntity;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm")
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm")
    @Column(name = "arrival_time_init")
    private LocalDateTime arrivalTimeInit;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm")
    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "departure_time_init")
    private LocalDateTime departureTimeInit;

    @Enumerated(EnumType.STRING)
    @Column(name = "delay",nullable = false)
    private EStatus delayed;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private EStatus status;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "train_entity_id", nullable = false)
    private TrainEntity trainEntity;


    @Override
    protected void onCreate() {
        super.onCreate();
        this.arrivalTimeInit = this.arrivalTime;
        this.departureTimeInit = this.departureTime;
        this.delayed=EStatus.schedule;
    }




}
