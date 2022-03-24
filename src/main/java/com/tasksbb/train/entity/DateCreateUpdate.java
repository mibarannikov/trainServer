package com.tasksbb.train.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class DateCreateUpdate {


    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
