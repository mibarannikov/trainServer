package com.tasksbb.train;

import com.tasksbb.train.entity.PointOfScheduleEntity;
import com.tasksbb.train.repository.PointOfScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@SpringBootTest
class TrainApplicationTests {

    @Autowired
    public PointOfScheduleRepository pointOfScheduleRepository;

    @Test
    void test5(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime d = now.plusMinutes(5);
        Long newd  = d.toEpochSecond(ZoneOffset.of("+0"))-(now.toEpochSecond(ZoneOffset.of("+0")));
        System.out.println("должно быть 5 минут" +newd);

    }

    @Test
    void test6(){
        List<PointOfScheduleEntity> points = pointOfScheduleRepository.findAll();
        points.forEach(p->{
            p.setArrivalTimeInit(p.getArrivalTime());
            p.setDepartureTimeInit(p.getDepartureTime());

        });



    }

}
