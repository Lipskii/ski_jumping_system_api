package com.lipskii.ski_jumping_system.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "city")
public class City implements Comparable<City> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcity")
    private int id;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties("cities")
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "region_idregion")
    private Region region;

    @JsonIgnoreProperties({"city","skiJumpers","venues"})
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    // @Fetch(value = FetchMode.SUBSELECT)
    private List<SkiClub> skiClubs;

    @JsonIgnoreProperties({"city","skiClub"})
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Venue> venues;

    @JsonIgnoreProperties("city")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "city", cascade = {CascadeType.PERSIST})
    private List<Person> people;

    public City() {
    }

    public City(String name, Region region) {
        this.name = name;
        this.region = region;
    }

    public City(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<SkiClub> getSkiClubs() {
        return skiClubs;
    }

    public void setSkiClubs(List<SkiClub> skiClubs) {
        this.skiClubs = skiClubs;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    @Override
    public int compareTo(City o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region=" + region +
                '}';
    }
}
