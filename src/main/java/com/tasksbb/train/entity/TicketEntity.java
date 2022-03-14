package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_entity")
@Getter
@Setter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seat_entity_id")
    private SeatEntity seatEntity;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_entity_id")
    private PassengerEntity passengerEntity;

    //@ManyToOne(cascade = CascadeType.ALL, optional = false)
   // @JoinColumn(name = "train_owner_id", nullable = false)
    //private TrainEntity trainOwner;

// @ManyToOne(cascade = CascadeType.ALL)
   // @JoinTable(name = "ticket_entity_train_entities",
   //         joinColumns = @JoinColumn(name = "ticket_entity_id"),
   //         inverseJoinColumns = @JoinColumn(name = "train_entities_id"))
   // private TrainEntity ownerTrain;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "ticket_entity_point_of_schedules",
            joinColumns = @JoinColumn(name = "ticket_entity_id"),
            inverseJoinColumns = @JoinColumn(name = "point_of_schedules_id"))
    private List<PointOfScheduleEntity> pointOfSchedules = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    private Long version;
    //@OneToOne(mappedBy = "ticketEntity", cascade = CascadeType.ALL, orphanRemoval = true)
   // private PassengerEntity passengerEntity;

}
