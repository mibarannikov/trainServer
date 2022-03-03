package com.tasksbb.train.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seat_entity")
@Getter
@Setter
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_entity_id")
    private TrainEntity trainEntity;

    @OneToMany(mappedBy = "seatEntity", orphanRemoval = true)
    private List<TicketEntity> tickets = new ArrayList<>();


}
