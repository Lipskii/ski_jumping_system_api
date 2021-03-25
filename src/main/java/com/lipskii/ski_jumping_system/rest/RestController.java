package com.lipskii.ski_jumping_system.rest;


import com.lipskii.ski_jumping_system.dto.*;
import com.lipskii.ski_jumping_system.entity.*;
import com.lipskii.ski_jumping_system.service.*;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

//TODO error handling

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final AllTimePointsSystemService allTimePointsSystemService;
    private final CityService cityService;
    private final CountryService countryService;
    private final CompetitionService competitionService;
    private final DisqualificationTypeService disqualificationTypeService;
    private final GenderService genderService;
    private final HillService hillService;
    private final HillVersionService hillVersionService;
    private final JuryService juryService;
    private final JuryTypeService juryTypeService;
    private final PersonService personService;
    private final RegionService regionService;
    private final ResultService resultService;
    private final SeasonService seasonService;
    private final SeriesService seriesService;
    private final SizeOfHillService sizeOfHillService;
    private final SkiClubService skiClubService;
    private final SkiJumperService skiJumperService;
    private final SkisService skisService;
    private final VenueService venueService;
    private final WeatherService weatherService;


    @Autowired
    public RestController(AllTimePointsSystemService allTimePointsSystemService, CityService cityService, CountryService countryService, CompetitionService competitionService, DisqualificationTypeService disqualificationTypeService, GenderService genderService, HillService hillService, HillVersionService hillVersionService, JuryService juryService, JuryTypeService juryTypeService, PersonService personService, RegionService regionService, ResultService resultService, SeasonService seasonService, SeriesService seriesService, SizeOfHillService sizeOfHillService, SkiClubService skiClubService, SkiJumperService skiJumperService, SkisService skisService, VenueService venueService, WeatherService weatherService) {
        this.allTimePointsSystemService = allTimePointsSystemService;
        this.cityService = cityService;
        this.countryService = countryService;
        this.competitionService = competitionService;
        this.disqualificationTypeService = disqualificationTypeService;
        this.genderService = genderService;
        this.hillService = hillService;
        this.hillVersionService = hillVersionService;
        this.juryService = juryService;
        this.juryTypeService = juryTypeService;
        this.personService = personService;
        this.regionService = regionService;
        this.resultService = resultService;
        this.seasonService = seasonService;
        this.seriesService = seriesService;
        this.sizeOfHillService = sizeOfHillService;
        this.skiClubService = skiClubService;
        this.skiJumperService = skiJumperService;
        this.skisService = skisService;
        this.venueService = venueService;
        this.weatherService = weatherService;
    }


    @Transactional
    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<City> getCities(
            @Join(path = "people", alias = "o")
            @And({
                    @Spec(path = "id", params = "id", spec = Equal.class),
                    @Spec(path = "name", params = "name", spec = Equal.class),
                    @Spec(path = "region.id", params = "regionId", spec = Equal.class),
                    @Spec(path = "region.country.id", params = "countryId", spec = Equal.class),
                    @Spec(path="o", params="hasPeople", spec=NotNull.class)
            }) Specification<City> spec) {
        return cityService.get(spec, Sort.by("name"));
    }

    @PostMapping("/city")
    public City addCity(@RequestBody City city) {
        //city.setName(city.getName().trim());
        cityService.save(city);
        return city;
    }

    @GetMapping("/competitions/hill/{hillId}")
    public List<Competition> getCompetitionsByHill(@PathVariable("hillId") int hillId) {
        return competitionService.findAllByHillId(hillId);
    }

    @GetMapping("/competitions/hillAndSeries/{hillId}&{seriesId}")
    public List<Competition> getCompetitionsByHillAndSeries(@PathVariable("seriesId") int seriesId, @PathVariable("hillId") int hillId) {
        return competitionService.findAllBySeriesAndHillId(hillId, seriesId);
    }

    @Transactional
    @GetMapping(value = "/competitions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Competition> getCompetitions(
            @And({
                    @Spec(path = "season.id", params = "seasonId", spec = Equal.class),
                    @Spec(path = "seriesMajor.id", params = "seriesMajorId", spec = Equal.class),
                    @Spec(path = "seriesMinor.id", params = "seriesMinorId", spec = Equal.class),
                    @Spec(path = "hillVersion.hill.id", params = "hillId", spec = Equal.class)
            }) Specification<Competition> spec) {
        return competitionService.get(spec, Sort.by(Sort.Direction.DESC, "date1"));
    }

    @GetMapping("/competitions/series/{seriesId}")
    public List<Competition> getCompetitionsBySeries(@PathVariable("seriesId") int seriesId) {
        return competitionService.findAllBySeriesId(seriesId);
    }

    @GetMapping("/competitions/season/{seasonId}")
    public List<Competition> getCompetitionsBySeason(@PathVariable("seasonId") int seasonId) {
        return competitionService.findAllBySeasonId(seasonId);
    }

    @PostMapping("/competitions")
    public Competition addCompetition(@RequestBody Competition competition) {
        competitionService.save(competition);
        return competition;
    }

    @GetMapping("/countries")
    public List<Country> getCountries() {
        return countryService.findAll();
    }

    @GetMapping("/countries/jury")
    public List<Country> getCountriesWithJury() {
        return countryService.findAllWithJury();
    }

    @GetMapping("/countries/people")
    public List<Country> getCountriesWithPeople() {
        return countryService.findAllWithPeople();
    }

    @GetMapping("/countries/skiClubs")
    public List<Country> getCountriesWithSkiClubs() {
        return countryService.findAllWithSkiClubs();
    }

    @GetMapping("/countries/skiJumpers")
    public List<Country> getCountriesWithSkiJumpers() {
        return countryService.findAllWithSkiJumpers();
    }

    @GetMapping("/countries/venues")
    public List<Country> getCountriesWithVenues() {
        return countryService.findAllWithVenues();
    }

    @GetMapping("/countries/venues/hills")
    public List<Country> getCountriesWithVenuesWithHills() {
        return countryService.findAllWithVenuesWithHills();
    }

    @GetMapping("/genders")
    public List<Gender> getGenders() {
        return genderService.findAll();
    }

    @GetMapping("/hills")
    public List<Hill> getHills() {
        return hillService.findAll();
    }

    @PostMapping("/hills")
    public Hill addHill(@RequestBody Hill hill) {
        hillService.save(hill);
        return hill;
    }

    @PutMapping("hills/{hillId}")
    public ResponseEntity<Hill> updateHill(@PathVariable("hillId") int hillId, @RequestBody Hill requestHill) throws ResourceNotFoundException {

        if (hillService.findById(hillId).isPresent()) {
            requestHill.setId(hillId);
            hillService.save(requestHill);
        } else {
            throw new ResourceNotFoundException("No hill found for id: " + hillId);
        }

        return ResponseEntity.ok(requestHill);
    }

    @GetMapping("/hills/{venueId}")
    public List<Hill> getHillsByVenue(@PathVariable("venueId") int venueId) {
        return hillService.findAllByVenueId(venueId);
    }

    @GetMapping("/hillVersions")
    public List<HillVersion> getHillVersions() {
        return hillVersionService.findAll();
    }


    @PostMapping("/hillVersions")
    public HillVersion addHillVersion(@RequestBody HillVersion hillVersion) {
        System.out.println(hillVersion);
        hillVersionService.save(hillVersion);
        return hillVersion;
    }

    @GetMapping("/jury")
    public List<JuryDTO> getJury() {
        return juryService.findAllDTO();
    }

    @GetMapping("/jury/rd")
    public List<JuryDTO> getRDs() {
        return juryService.findAllRaceDirectors();
    }

    @GetMapping("/jury/td")
    public List<JuryDTO> getTDs() {
        return juryService.findAllTechnicalDelegates();
    }

    @GetMapping("/jury/coc")
    public List<JuryDTO> getChiefs() {
        return juryService.findAllChiefsOfCompetitions();
    }

    @GetMapping("/jury/ec")
    public List<JuryDTO> getEquipmentControllers() {
        return juryService.findAllEquipmentControllers();
    }

    @GetMapping("/jury/judges")
    public List<JuryDTO> getJudges() {
        return juryService.findAllJudges();
    }

    @GetMapping("/jury/atd")
    public List<JuryDTO> getTDAssistants() {
        return juryService.findAllTDAssistants();
    }

    @GetMapping("/jury/ard")
    public List<JuryDTO> getRDAssistants() {
        return juryService.findAllRDAssistants();
    }

    @PostMapping("/jury")
    public Jury saveJury(@RequestBody Jury jury) {
        juryService.save(jury);
        return jury;
    }

    @GetMapping("/jury/country/{countryId}")
    public List<JuryDTO> getJuryByCountry(@PathVariable("countryId") int countryId) {
        return juryService.findAllByCountryId(countryId);
    }

    @GetMapping("/juryTypes")
    public List<JuryType> getJuryTypes() {
        return juryTypeService.findAll();
    }

    @GetMapping("/people")
    public List<Person> getPeople() {
        return personService.findAllOrderedByLastName();
    }

    @GetMapping("/people/country/{countryId}")
    public List<Person> getPeopleByCountryId(@PathVariable("countryId") int countryId) {
        return personService.findAllByCountryId(countryId);
    }

    @PostMapping("/people")
    public Person addPerson(@RequestBody Person person) {
        personService.save(person);
        return person;
    }

    @PostMapping(value = "/people/photo/{personId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadPersonPhoto(@RequestPart MultipartFile file, @PathVariable("personId") int personId)
            throws ResourceNotFoundException {
        Person person = personService.findById(personId).orElseThrow(() -> new ResourceNotFoundException("No person found for id " + personId));
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FilesPaths.ATHLETES_PHOTOS_PATH + personId + "_"
                    + person.getFirstName() + "_" + person.getLastName());
            Files.write(path, bytes);
            person.setPhoto(file.getOriginalFilename());
            personService.save(person);
        } catch (IOException e) {
            System.out.println(e.getCause().getMessage());
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/people/{personId}")
    public Person updatePerson(@RequestBody Person person, @PathVariable("personId") int personId) throws ResourceNotFoundException {

        if (personService.findById(personId).isPresent()) {
            person.setId(personId);
            personService.save(person);
        } else {
            throw new ResourceNotFoundException("No person found for id: " + personId);
        }

        return person;
    }

    @GetMapping("/regions")
    public List<Region> getRegions() {
        return regionService.findAllOrderByName();
    }

    @GetMapping("/regions/country/{countryId}")
    public List<Region> getRegionsByCountry(@PathVariable("countryId") int countryId) {
        return regionService.getRegionsByCountry(countryId);
    }

    @GetMapping("/results/{competitionId}")
    public List<Result> getResultsByCompetitionId(@PathVariable("competitionId") int competitionId) {
        return resultService.findAllByCompetitionId(competitionId);
    }

    @PostMapping(value = "/results/files/{competitionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadResultsCsv(@RequestPart("csv") MultipartFile csvFile,
                                           @RequestPart("pdf") MultipartFile pdfFile,
                                           @PathVariable("competitionId") int competitionId)
            throws ResourceNotFoundException {
        Competition competition = competitionService
                .findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("no competition found for id = " + competitionId));

        try {
            competitionService.assignFiles(csvFile, pdfFile, competition, competitionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/seasons")
    public List<Season> getSeasons() {
        return seasonService.findAll();
    }

    @GetMapping("/series")
    public List<Series> getSeries() {
        return seriesService.findAll();
    }

    @GetMapping("/sizeOfHill")
    public List<SizeOfHill> getSizesOfHil() {
        return sizeOfHillService.findAll();
    }

    @GetMapping("/skiClubs")
    public List<SkiClubDTO> getSkiClubs() {
        return skiClubService.findAllOrderByNameDTO();
    }

    @PostMapping("/skiClubs")
    public SkiClub addSkiClubs(@RequestBody SkiClub skiClub) {
        skiClubService.save(skiClub);
        return skiClub;
    }

    @PutMapping("/skiClubs/{skiClubId}")
    public ResponseEntity<SkiClub> updateSkiClub(@PathVariable("skiClubId") int skiClubId, @RequestBody SkiClub skiClub)
            throws ResourceNotFoundException {
        if (skiClubService.findById(skiClubId).isPresent()) {
            skiClub.setId(skiClubId);
            skiClubService.save(skiClub);
        } else {
            throw new ResourceNotFoundException("No ski club found for id: " + skiClubId);
        }

        return ResponseEntity.ok(skiClub);
    }

    @GetMapping("/skiClubs/city/{cityId}")
    public List<SkiClubDTO> getSkiClubsByCity(@PathVariable("cityId") int cityId) {
        return skiClubService.findAllByCityIdDTO(cityId);
    }

    @GetMapping("/skiClubs/country/{countryId}")
    public List<SkiClubDTO> getSkiClubsByCountry(@PathVariable("countryId") int countryId) {
        return skiClubService.findAllByCountryIdDTO(countryId);
    }

    @Transactional
    @GetMapping(value = "/skiJumpers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<SkiJumper> getSkiJumpers(
            @And({
                    @Spec(path = "id", params = "id", spec = Equal.class),
                    @Spec(path = "person.id", params = "personId", spec = Equal.class),
                    @Spec(path = "isActive", params = "isActive", spec = Equal.class),
                    @Spec(path = "fisCode", params = "fisCode", spec = Equal.class),
                    @Spec(path = "skis.id", params = "skisId", spec = Equal.class),
                    @Spec(path = "skiClub.id", params = "skiClubId", spec = Equal.class),
                    @Spec(path = "person.country.id", params = "countryId", spec = Equal.class),
                    @Spec(path = "person.city.id", params = "cityId", spec = Equal.class),
            }) Specification<SkiJumper> spec) {
        return skiJumperService.get(spec, Sort.by("person.lastName"));
    }

    @PostMapping("/skiJumpers")
    public SkiJumper addSkiJumper(@RequestBody SkiJumper skiJumper) {
        skiJumperService.save(skiJumper);
        return skiJumper;
    }

    @PutMapping("/skiJumpers/{skiJumperId}")
    public SkiJumper updateSkiJumper(@RequestBody SkiJumper skiJumper, @PathVariable("skiJumperId") int skiJumperId) throws ResourceNotFoundException {

        if (skiJumperService.findById(skiJumperId).isPresent()) {
            skiJumper.setId(skiJumperId);
            skiJumperService.save(skiJumper);
        } else {
            throw new ResourceNotFoundException("No jumper found for id: " + skiJumperId);
        }

        return skiJumper;
    }

    @GetMapping("/skiJumpers/city/{cityId}")
    public List<SkiJumperDTO> getSkiJumpersByCity(@PathVariable("cityId") int cityId) {
        return skiJumperService.findAllByCityIdDTO(cityId);
    }

    @GetMapping("/skiJumpers/country/{countryId}")
    public List<SkiJumperDTO> getSkiJumpersByCountry(@PathVariable("countryId") int countryId) {
        return skiJumperService.findAllByCountryIdDTO(countryId);
    }

    @GetMapping("/skis")
    public List<Skis> getSkis() {
        return skisService.findAll();
    }

    @GetMapping("/venues")
    public List<VenueDTO> getVenues() {
        return venueService.findAllDTO();
    }

    @GetMapping("/venues/hills")
    public List<VenueDTO> getVenuesWithHills() {
        return venueService.findAllWithHillsDTO();
    }

    @PostMapping("/venues")
    public Venue saveVenue(@RequestBody Venue venue) {
        venueService.save(venue);
        return venue;
    }

    @PutMapping("/venues/{venueId}")
    public ResponseEntity<Venue> updateVenue(@RequestBody Venue venue, @PathVariable("venueId") int venueId) throws ResourceNotFoundException {

        if (venueService.findById(venueId).isPresent()) {
            venue.setId(venueId);
            venueService.save(venue);
        } else {
            throw new ResourceNotFoundException("No venue found for id: " + venueId);
        }

        return ResponseEntity.ok(venue);
    }

    @DeleteMapping("/venues/{venueId}")
    public Map<String, Boolean> deleteVenue(@PathVariable("venueId") int venueId) {

        Venue venue = venueService.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found for this id :: " + venueId));

        System.out.println(venue);
        venueService.delete(venue);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;

    }

    @GetMapping("/venues/city/{cityId}")
    public List<VenueDTO> getVenuesByCity(@PathVariable("cityId") int cityId) {
        return venueService.getVenuesByCity(cityId);
    }

    @GetMapping("/venues/country/{countryId}")
    public List<VenueDTO> getVenuesByCountry(@PathVariable("countryId") int countryId) {
        return venueService.findAllByCountryDTO(countryId);
    }

    @GetMapping("/weather")
    public List<Weather> getWeather() {
        return weatherService.findAll();
    }

}
