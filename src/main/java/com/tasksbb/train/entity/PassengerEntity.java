package com.tasksbb.train.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "passenger_entity")
@Getter
@Setter
public class PassengerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

   //@OneToOne
   // private TicketEntity ticketEntity;


   // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
   // @JoinColumn(name = "ticket_entity_id")
   // private TicketEntity ticketEntity;

}
