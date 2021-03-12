package com.lipskii.ski_jumping_system.dto;

import com.lipskii.ski_jumping_system.entity.City;

public class SkiClubDTO {

    private Integer id;
    private String name;
    private City city;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
