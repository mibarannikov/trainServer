package com.tasksbb.train.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "station_entity")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"canGetStations"})
public class StationEntity extends DateCreateUpdate {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name_station", nullable = false, unique = true)
    private String nameStation;


    @Column(name = "latitude", nullable = false)
    private Double latitude;


    @Column(name = "longitude", nullable = false)
    private Double longitude;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "station_entity_can_get_station",
            joinColumns = @JoinColumn(name = "station_entity_1_id"),
            inverseJoinColumns = @JoinColumn(name = "station_entities_2_id"))
    private Set<StationEntity> canGetStations = new LinkedHashSet<>();


}
