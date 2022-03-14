package com.tasksbb.train.service;

import com.tasksbb.train.entity.PassengerEntity;
import com.tasksbb.train.entity.TicketEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.ex.TrainNotFoundException;
import com.tasksbb.train.repository.TicketEntityRepository;
import com.tasksbb.train.repository.TrainEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

public class PassengerService {

    public final TicketEntityRepository ticketEntityRepository;

    public final TrainEntityRepository trainEntityRepository;

    public PassengerService(TicketEntityRepository ticketEntityRepository, TrainEntityRepository trainEntityRepository) {
        this.ticketEntityRepository = ticketEntityRepository;
        this.trainEntityRepository = trainEntityRepository;
    }



}
