package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seat_entity")
@Getter
@Setter
public class SeatEntity extends DateCreateUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private Long seatNumber;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "train_entity_id")
    private TrainEntity trainEntity;

    @OneToMany(mappedBy = "seatEntity", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TicketEntity> tickets = new ArrayList<>();


}
