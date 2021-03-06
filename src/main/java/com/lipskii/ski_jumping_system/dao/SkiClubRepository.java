package com.lipskii.ski_jumping_system.dao;

import com.lipskii.ski_jumping_system.entity.City;
import com.lipskii.ski_jumping_system.entity.Country;
import com.lipskii.ski_jumping_system.entity.SkiClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiClubRepository extends JpaRepository<SkiClub,Integer>, JpaSpecificationExecutor<SkiClub> {

    List<SkiClub> findAllByCityRegionCountryOrderByName(Country country);

    List<SkiClub> findAllByCityOrderByName(City city);

    List<SkiClub> findAllByCityIdOrderByName(int cityId);

    List<SkiClub> findAllByCityRegionCountryIdOrderByName(int countryId);

    List<SkiClub> findAllByOrderByName();


}
