package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "station_entity")
@Getter
@Setter
public class StationEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "train_entity_id")
    private TrainEntity trainEntity;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "station_entity_id")
    private List<StationEntity> stationEntities = new ArrayList<>();




}
