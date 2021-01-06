package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public PathFinderResponse findShortestPath(final long departureId, final long arrivalId) {
        final Station departureStation = stationRepository.findById(departureId).orElseThrow(NotFoundException::new);
        final Station arrivalStation = stationRepository.findById(arrivalId).orElseThrow(NotFoundException::new);

        final PathFinder pathFinder = PathFinder.of(sectionRepository.findAll());

        final GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(departureStation, arrivalStation);
        return PathFinderResponse.of(shortestPath);
    }
}
