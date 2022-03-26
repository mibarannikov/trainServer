package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class WagonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wagon_number")
    private Long wagonNumber;

    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String nameWagon;
    @Column(nullable = false)
    private Long sumSeats;
    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "train_entity_id")
    private TrainEntity trainEntity;

}
