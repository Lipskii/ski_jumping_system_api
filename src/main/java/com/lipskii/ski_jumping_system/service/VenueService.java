package com.lipskii.ski_jumping_system.service;

import com.lipskii.ski_jumping_system.dao.VenueRepository;
import com.lipskii.ski_jumping_system.dto.SkisDTO;
import com.lipskii.ski_jumping_system.entity.Country;
import com.lipskii.ski_jumping_system.entity.Skis;
import com.lipskii.ski_jumping_system.entity.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenueService implements ServiceInterface {

    private final VenueRepository venueRepository;
    private final HillService hillService;

    @Autowired
    public VenueService(VenueRepository venueRepository, HillService hillService) {
        this.venueRepository = venueRepository;
        this.hillService = hillService;
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAllByOrderByName();
    }

    public List<Venue> findAllByCountry(int id) {
        return venueRepository.findAllByCityRegionCountryIdOrderByName(id);
    }

    @Override
    public Optional<Venue> findById(int id) {
        return venueRepository.findById(id);
    }

    @Override
    public Venue save(Object obj) {
        return venueRepository.save((Venue) obj);
    }

    @Override
    public void deleteById(int id) {
        venueRepository.deleteById(id);
    }

    @Transactional
    public boolean deleteByIdBool(int id) {
        if(venueRepository.existsById(id)){

            venueRepository.deleteById(id);
           // return true;
        }
        //return false;

        return !venueRepository.existsById(id);
    }


    List<Venue> findAllByCountry(Country country){
        return venueRepository.findAllByCityRegionCountry(country);
    }

}
