package com.lipskii.ski_jumping_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "person")
public class Person implements Comparable<Person> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idperson")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnoreProperties("people")
    @JoinColumn(name = "gender_idgender")
    private Gender gender;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    /**
     * Current country that athlete represents with his fis license.
     */
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnoreProperties({"people","cities"})
    @JoinColumn(name = "country_idcountry")
    private Country country;

    /**
     * Place of birth.
     */
    @JsonIgnoreProperties("people")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "city_idcity")
    private City city;

    @JsonIgnoreProperties("person")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person")
    private List<Jury> juryList;

    @Column(name = "photo")
    private String photo;

    public Person() {
    }

    public Person(String firstName, String lastName, Gender gender, LocalDate birthdate, Country country, City city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.country = country;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
//
//    public List<Jury> getJuryList() {
//        return juryList;
//    }
//
//    public void setJuryList(List<Jury> juryList) {
//        this.juryList = juryList;
//    }

    @Override
    public int compareTo(Person o) {
        if(!this.lastName.equals(o.lastName)){
            return this.lastName.compareTo(o.lastName);
        }
        return this.firstName.compareTo(o.firstName);
    }

    @Override
    public String toString() {
        return firstName + lastName + country.getName();
    }
}
