package com.tasksbb.train.service;

import com.tasksbb.train.entity.PassengerEntity;
import com.tasksbb.train.entity.TrainEntity;
import com.tasksbb.train.repository.TicketEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PassengerServiceTest {

    private TicketEntityRepository ticketEntityRepository;
    private PassengerService passengerService;

    @BeforeEach
    private void init() {
        TicketEntityRepository ticketEntityRepository = Mockito.mock(TicketEntityRepository.class);
        passengerService = new PassengerService(ticketEntityRepository);
    }

    @Test
    void passengerIsPresent() {
        when(ticketEntityRepository.findBySeatEntity_TrainEntity_TrainNumber(ArgumentMatchers.anyLong())).thenReturn(new ArrayList<>());
        passengerService.passengerIsPresent(
                ArgumentMatchers.any(TrainEntity.class),
                ArgumentMatchers.any(PassengerEntity.class),
                ArgumentMatchers.any(ArrayList.class));
        verify(ticketEntityRepository,times(1)).findBySeatEntity_TrainEntity_TrainNumber(ArgumentMatchers.anyLong());
    }
}
