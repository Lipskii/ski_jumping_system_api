package com.lipskii.ski_jumping_system.service;

import com.lipskii.ski_jumping_system.dao.CompetitionRepository;
import com.lipskii.ski_jumping_system.entity.Competition;
import com.lipskii.ski_jumping_system.entity.Hill;
import com.lipskii.ski_jumping_system.entity.Series;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService implements ServiceInterface {


    private final CompetitionRepository competitionRepository;
    private final HillService hillService;
    private final TeamOverallStandingService teamOverallStandingService;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository,
                              HillService hillService,
                              @Lazy TeamOverallStandingService teamOverallStandingService) {
        this.competitionRepository = competitionRepository;
        this.hillService = hillService;
        this.teamOverallStandingService = teamOverallStandingService;
    }

    @Override
    public List<Competition> findAll() {
        return competitionRepository.findAllByOrderByDate1Desc();
    }

    public List<Competition> get(Specification<Competition> spec, Sort sort) {
        return competitionRepository.findAll(spec, sort);
    }

    public List<Competition> findAllBySeriesId(int seriesId){
        return competitionRepository.findAllBySeriesMajorIdOrderByDate1Desc(seriesId);
    }

    public List<Competition> findAllBySeriesMajorAndSeason(Series series, int season){
        return competitionRepository.findAllBySeasonSeasonAndSeriesMajorOrderByDate1(season, series);
    }

    public List<Competition> findAllByHillId(int hillId){
        Hill hill = hillService.findById(hillId).orElseThrow(() -> new ResourceNotFoundException("No hill found for id = " + hillId));
        return competitionRepository.findAllByHillVersionHillOrderByDate1Desc(hill);
    }

    public List<Competition> findAllBySeriesAndHillId(int hillId, int seriesId) {
        Hill hill = hillService.findById(hillId).orElseThrow(() -> new ResourceNotFoundException("No hill found for id = " + hillId));
        return competitionRepository.findAllBySeriesMajorIdAndHillVersionHillOrderByDate1Desc(seriesId,hill);
    }


    @Override
    public Optional<Competition> findById(int id) {
        return competitionRepository.findById(id);
    }

    public Competition updateCompetition(int competitionId, Competition competition){
        if (findById(competitionId).isPresent()) {
            competition.setId(competitionId);
            save(competition);
        } else {
            throw new ResourceNotFoundException("No competition found for id: " + competitionId);
        }

        return competition;
    }

    @Override
    public Competition save(Object obj) {
        return competitionRepository.save((Competition) obj);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        Competition competition = competitionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("competition does not exist!"));
        if(competition.getSeriesMajor().getId() == 9){
            teamOverallStandingService.teamOverallStandingsSubtractionByIndCompetition(competition);
        }
        competitionRepository.delete(competition);
    }

    public List<Competition> findAllBySeasonId(int seasonId) {
        return competitionRepository.findAllBySeasonId(seasonId);
    }


    public List<Competition> findAllBySeriesMinorAndSeason(Series series, int season) {
        return competitionRepository.findAllBySeasonSeasonAndSeriesMinorOrderByDate1(season, series);
    }
}
