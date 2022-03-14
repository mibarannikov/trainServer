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
public class TrainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "train_number", unique = true)
    private Long trainNumber;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm")
    @Column(name = "departure", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "train_speed", nullable = false)
    private Double trainSpeed;

    // @OneToMany(mappedBy = "trainEntity", orphanRemoval = true, cascade = CascadeType.ALL, )
    // private List<PointOfSchedule> pointOfSchedules = new ArrayList<>();


    @OneToMany(mappedBy = "trainEntity",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointOfScheduleEntity> pointOfSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "trainEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SeatEntity> seatEntities = new ArrayList<>();

    @Transient
    private Long amountOfEmptySeats;


   // @OneToMany(mappedBy = "trainOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<TicketEntity> ticketEntities = new ArrayList<>();

}
