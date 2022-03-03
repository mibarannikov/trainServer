package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "train_entity")
@Getter
@Setter
public class TrainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "train_number", unique = true)
    private Long trainNumber;

    @OneToMany(mappedBy = "trainEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointOfSchedule> pointsOfSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "trainEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StationEntity> stationsTrace = new ArrayList<>();

    @OneToMany(mappedBy = "trainEntity", orphanRemoval = true)
    private List<SeatEntity> seatEntities = new ArrayList<>();





}
