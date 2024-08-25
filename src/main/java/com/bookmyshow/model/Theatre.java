package com.bookmyshow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Theatre extends BaseModel {
    private String name;
    private String address;

    @ManyToOne
    private City city;

    @OneToMany
    private List<Auditorium> auditoriums;
}
