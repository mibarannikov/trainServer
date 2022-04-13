package com.tasksbb.train.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.tasksbb.train.dto.TicketDto;
import com.tasksbb.train.entity.*;

import com.tasksbb.train.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


class TicketServiceTest {
    private ObjectMapper mapper = new ObjectMapper();

    private TicketService ticketService;

    private SeatEntityRepository seatEntityRepository;

    private TicketEntityRepository ticketEntityRepository;

    private PointOfScheduleRepository pointOfScheduleRepository;

    private PassengerEntityRepository passengerEntityRepository;

    private TrainEntityRepository trainEntityRepository;

    private StationService stationService;

    @BeforeEach
    private void init() {
        seatEntityRepository = mock(SeatEntityRepository.class);
        ticketEntityRepository = mock(TicketEntityRepository.class);
        pointOfScheduleRepository = mock(PointOfScheduleRepository.class);
        passengerEntityRepository = mock(PassengerEntityRepository.class);
        trainEntityRepository = mock(TrainEntityRepository.class);
        stationService = mock(StationService.class);
        ticketService = new TicketService(seatEntityRepository,
                ticketEntityRepository,
                pointOfScheduleRepository,
                passengerEntityRepository,
                trainEntityRepository,
                stationService);

    }

    private TicketDto createTicketDto() throws URISyntaxException, IOException {
        mapper.registerModule(new JSR310Module());
        URL resource = getClass().getClassLoader().getResource("ticket_dto.json");
        Path path = Paths.get(resource.toURI());
        TicketDto ticketDto = mapper.readValue(path.toFile(), TicketDto.class);
        return ticketDto;

    }

//    @Test
//    void buyTicket() throws URISyntaxException, IOException {
//        TicketDto ticketDto = createTicketDto();
//        TrainEntity trainEntity = new TrainEntity();
//        SeatEntity seat = new SeatEntity();
//        trainEntity.setTrainNumber(111L);
//        seat.setTrainEntity(trainEntity);
//
//        ticketDto.getNameStations().get(0).setDepartureTime(LocalDateTime.now().plusMinutes(12));
//        when(trainEntityRepository.findByTrainNumber(ArgumentMatchers.anyLong())).thenReturn(Optional.of(trainEntity));
//        when(seatEntityRepository.findByTrainEntityTrainNumberAndSeatNumber(
//                ArgumentMatchers.anyLong(),
//                ArgumentMatchers.anyLong()))
//                .thenReturn(Optional.of(new SeatEntity()));
//        when(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(
//                ArgumentMatchers.any(TrainEntity.class),
//                ArgumentMatchers.anyString()))
//                .thenReturn(Optional.of(new PointOfScheduleEntity()));
//        when(seatEntityRepository
//                .findByTrainEntityTrainNumberAndSeatNumber(anyLong(),anyLong()))
//                .thenReturn(Optional.of(seat));
//        when(pointOfScheduleRepository.findByTrainEntityAndStationEntityNameStation(any(TrainEntity.class),anyString()))
//                .thenReturn(Optional.of(new PointOfScheduleEntity()));
//        when(passengerEntityRepository.findByFirstnameAndLastnameAndDateOfBirth(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(LocalDate.class))).thenReturn(Optional.empty());
////        when(passengerService.passengerIsPresent(ArgumentMatchers.any(TrainEntity.class),
////                ArgumentMatchers.any(PassengerEntity.class),
////                ArgumentMatchers.anyList())).thenReturn(false);
//        when(ticketEntityRepository.save(ArgumentMatchers.any(TicketEntity.class))).thenReturn(new TicketEntity());
//      //  when(TicketFacade.ticketToTicketDto(any(TicketEntity.class))).thenReturn(ticketDto);
//        ticketService.buyTicket(ticketDto, new User());
//        verify(trainEntityRepository, times(1)).findByTrainNumber(ArgumentMatchers.anyLong());
//        verify(seatEntityRepository, times(1)).findByTrainEntityTrainNumberAndSeatNumber(
//                ArgumentMatchers.anyLong(),
//                ArgumentMatchers.anyLong());
//        verify(pointOfScheduleRepository, times(3))
//                .findByTrainEntityAndStationEntityNameStation(
//                        ArgumentMatchers.any(TrainEntity.class),
//                        ArgumentMatchers.anyString());
//        verify(passengerEntityRepository, times(1))
//                .findByFirstnameAndLastnameAndDateOfBirth(
//                        ArgumentMatchers.anyString(),
//                        ArgumentMatchers.anyString(),
//                        ArgumentMatchers.any(LocalDate.class));
//    }

    @Test
    void getAllUserTickets() {
        when(ticketEntityRepository.findAllByUser(ArgumentMatchers.any(User.class))).thenReturn(new ArrayList<>());
        ticketService.getAllUserTickets(new User(), "act");
        verify(ticketEntityRepository, times(1)).findAllByUser(ArgumentMatchers.any(User.class));
    }

    @Test
    void allTrainTickets() {
        when(ticketEntityRepository.findBySeatEntity_TrainEntity_TrainNumber(ArgumentMatchers.any(Long.class))).thenReturn(new ArrayList<>());
        ticketService.AllTrainTickets(ArgumentMatchers.any(Long.class));
        verify(ticketEntityRepository, times(1)).findBySeatEntity_TrainEntity_TrainNumber(ArgumentMatchers.anyLong());
    }

    @Test
    void ticketsOnTheTrainNow() {
        PointOfScheduleEntity point = new PointOfScheduleEntity();
        List<PointOfScheduleEntity> points = new ArrayList<>();
        points.add(point);

        doReturn(points).when(pointOfScheduleRepository).findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(LocalDateTime.class)
        );

        doReturn(new ArrayList<>()).when(ticketEntityRepository).findAllByPointOfSchedules(ArgumentMatchers.any(PointOfScheduleEntity.class));

        ticketService.ticketsOnTheTrainNow(1L);
        verify(pointOfScheduleRepository, times(1)).findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(LocalDateTime.class));
        verify(ticketEntityRepository, times(1))
                .findAllByPointOfSchedules(ArgumentMatchers.any(PointOfScheduleEntity.class));

    }

    @Test
    void ticketsOnTheTrainNowEmpty() {
        PointOfScheduleEntity point = new PointOfScheduleEntity();
        List<PointOfScheduleEntity> points = new ArrayList<>();
        points.add(point);

        doReturn(new ArrayList<>()).when(pointOfScheduleRepository).findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(LocalDateTime.class)
        );

        doReturn(new ArrayList<>()).when(ticketEntityRepository).findAllByPointOfSchedules(ArgumentMatchers.any(PointOfScheduleEntity.class));

        ticketService.ticketsOnTheTrainNow(1L);
        verify(pointOfScheduleRepository, times(1)).findByTrainEntityTrainNumberAndDepartureTimeBeforeOrderByArrivalTimeAsc(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(LocalDateTime.class));
        verify(ticketEntityRepository, times(0))
                .findAllByPointOfSchedules(ArgumentMatchers.any(PointOfScheduleEntity.class));

    }

}
