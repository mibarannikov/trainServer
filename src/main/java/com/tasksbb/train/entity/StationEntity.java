package com.tasksbb.train.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "station_entity")
@Getter
@Setter
@NoArgsConstructor
public class StationEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name_station", nullable = false)
    private String nameStation;


    @Column(name = "latitude", nullable = false)
    private Double latitude;


    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "station_entity_can_get_station",
            joinColumns = @JoinColumn(name = "station_entity_1_id"),
            inverseJoinColumns = @JoinColumn(name = "station_entities_2_id"))
    private Set<StationEntity> canGetStations = new LinkedHashSet<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StationEntity station = (StationEntity) o;
        return id != null && Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
