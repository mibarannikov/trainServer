package com.tasksbb.train.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "train_entity")
@Getter
@Setter
public class TrainEntity extends DateCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "train_number", unique = true)
    private Long trainNumber;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm")
    @Column(name = "departure", nullable = false)
    private LocalDateTime departureTime;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm")
    @Column(name = "arrival_time_end")
    private LocalDateTime arrivalTimeEnd;

    @Column(name = "train_speed", nullable = false)
    private Double trainSpeed;

    @OneToMany(mappedBy = "trainEntity",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointOfScheduleEntity> pointOfSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "trainEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SeatEntity> seatEntities = new ArrayList<>();

    @OneToMany(mappedBy = "trainEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WagonEntity> wagonEntities = new ArrayList<>();

    @Transient
    private Long amountOfEmptySeats;

}
